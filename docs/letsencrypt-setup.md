# Let's Encrypt SSL Certificate Setup

This guide covers obtaining and installing a Let's Encrypt SSL certificate for the ILCDB webapp running in Docker/Podman with Tomcat.

## Prerequisites

- Domain name pointing to your server (e.g., `ilcdb.example.org`)
- Port 80 accessible from the internet for certificate validation
- Certbot installed on the host machine

## Step 1: Install Certbot

### On macOS:
```bash
brew install certbot
```

### On Linux (Ubuntu/Debian):
```bash
sudo apt-get update
sudo apt-get install certbot
```

## Step 2: Obtain Certificate

You have two main options:

### Option A: Standalone Mode (Recommended if webapp is not running)

Stop your containers temporarily:
```bash
docker-compose down
# or
podman-compose down
```

Obtain certificate using standalone mode (certbot runs its own web server on port 80):
```bash
sudo certbot certonly --standalone \
  -d ilcdb.example.org \
  --email admin@example.org \
  --agree-tos \
  --non-interactive
```

### Option B: Webroot Mode (Webapp keeps running)

This allows the webapp to keep running during certificate issuance.

First, ensure your Tomcat configuration serves the ACME challenge directory. You'll need to modify your setup to serve static files from a webroot directory.

```bash
sudo certbot certonly --webroot \
  -w /path/to/webroot \
  -d ilcdb.example.org \
  --email admin@example.org \
  --agree-tos
```

### Certificate Location

After successful validation, certificates are stored at:
- Certificate: `/etc/letsencrypt/live/ilcdb.example.org/fullchain.pem`
- Private Key: `/etc/letsencrypt/live/ilcdb.example.org/privkey.pem`
- Chain: `/etc/letsencrypt/live/ilcdb.example.org/chain.pem`

## Step 3: Convert PEM to JKS Format (for Tomcat)

Tomcat requires certificates in Java KeyStore (JKS) format. Convert the Let's Encrypt PEM files:

```bash
# Create PKCS12 keystore first
sudo openssl pkcs12 -export \
  -in /etc/letsencrypt/live/ilcdb.example.org/fullchain.pem \
  -inkey /etc/letsencrypt/live/ilcdb.example.org/privkey.pem \
  -out /etc/letsencrypt/live/ilcdb.example.org/keystore.p12 \
  -name tomcat \
  -passout pass:changeit

# Convert PKCS12 to JKS
sudo keytool -importkeystore \
  -deststorepass changeit \
  -destkeypass changeit \
  -destkeystore /etc/letsencrypt/live/ilcdb.example.org/keystore.jks \
  -srckeystore /etc/letsencrypt/live/ilcdb.example.org/keystore.p12 \
  -srcstoretype PKCS12 \
  -srcstorepass changeit \
  -alias tomcat
```

**IMPORTANT:** Replace `changeit` with a strong password and update `tomcat-config/server.xml` accordingly.

## Step 4: Mount Certificate in Container

Update your `compose.yaml` to mount the certificate:

```yaml
services:
  webapp:
    build: .
    container_name: ilcdb-webapp
    ports:
      - "80:8080"
      - "443:8443"
    volumes:
      - /etc/letsencrypt/live/ilcdb.example.org/keystore.jks:/usr/local/tomcat/conf/keystore.jks:ro
    depends_on:
      - db
    environment:
      MYSQL_HOST: db
      MYSQL_PORT: 3306
      MYSQL_DATABASE: ilcdb
      MYSQL_USER: ${MYSQL_USER}
      MYSQL_USER_PASSWORD: ${MYSQL_USER_PASSWORD}
```

## Step 5: Update Tomcat Configuration

Modify `tomcat-config/server.xml` to remove the self-signed certificate generation:

In `Dockerfile`, comment out or remove the keystore generation:
```dockerfile
# Remove or comment out these lines:
# COPY tomcat-config/generate-keystore.sh /tmp/generate-keystore.sh
# RUN chmod +x /tmp/generate-keystore.sh && /tmp/generate-keystore.sh
```

The `server.xml` already references `/usr/local/tomcat/conf/keystore.jks`, which will now be the Let's Encrypt certificate mounted from the host.

## Step 6: Update Domain in Config

Update `grails-app/conf/Config.groovy` to use your actual domain:

```groovy
environments {
    production {
        grails.serverURL = "https://ilcdb.example.org"
    }
}
```

## Step 7: Restart Containers

```bash
docker-compose up -d --build
# or
podman-compose up -d --build
```

## Step 8: Set Up Auto-Renewal

Let's Encrypt certificates expire after 90 days. Set up automatic renewal:

### Create Renewal Script

Create `/usr/local/bin/renew-ilcdb-cert.sh`:

```bash
#!/bin/bash

# Renew certificate
certbot renew --quiet

# Regenerate JKS keystore if certificate was renewed
if [ -f /etc/letsencrypt/live/ilcdb.example.org/fullchain.pem ]; then
    # Create PKCS12
    openssl pkcs12 -export \
      -in /etc/letsencrypt/live/ilcdb.example.org/fullchain.pem \
      -inkey /etc/letsencrypt/live/ilcdb.example.org/privkey.pem \
      -out /etc/letsencrypt/live/ilcdb.example.org/keystore.p12 \
      -name tomcat \
      -passout pass:changeit

    # Convert to JKS
    keytool -importkeystore \
      -deststorepass changeit \
      -destkeypass changeit \
      -destkeystore /etc/letsencrypt/live/ilcdb.example.org/keystore.jks \
      -srckeystore /etc/letsencrypt/live/ilcdb.example.org/keystore.p12 \
      -srcstoretype PKCS12 \
      -srcstorepass changeit \
      -alias tomcat \
      -noprompt

    # Restart container to load new certificate
    cd /path/to/ilcdb && docker-compose restart webapp
fi
```

Make it executable:
```bash
sudo chmod +x /usr/local/bin/renew-ilcdb-cert.sh
```

### Add Cron Job

```bash
sudo crontab -e
```

Add this line to run renewal check daily at 2 AM:
```
0 2 * * * /usr/local/bin/renew-ilcdb-cert.sh >> /var/log/letsencrypt-renewal.log 2>&1
```

## Testing

Visit your site at `https://ilcdb.example.org` and verify:
- Green padlock in browser
- Certificate issued by "Let's Encrypt"
- No browser warnings

## Troubleshooting

### Port 80 Not Accessible
If port 80 isn't accessible from the internet, use DNS-01 challenge instead:
```bash
sudo certbot certonly --manual --preferred-challenges dns \
  -d ilcdb.example.org \
  --email admin@example.org
```
Follow prompts to add TXT record to your DNS.

### Certificate Verification Failed
- Ensure domain points to your server IP
- Check firewall allows port 80
- Verify no other service is using port 80

### Keystore Conversion Errors
- Ensure OpenSSL and Java keytool are installed
- Check file permissions on `/etc/letsencrypt/live/`
- Run commands with sudo if needed
