# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

ILCDB is an immigration legal case database built with Grails 2.5.6. It manages client records, legal cases (intakes), appointments, and reporting for an immigration legal services organization. The application uses Spring Security for role-based access control and includes sophisticated reporting with caching and parallel processing optimizations.

## Build and Development Commands

### Local Development
```bash
# Run the application locally (defaults to HSQLDB in-memory)
grails run-app

# Build WAR file for deployment
grails war

# Clean build artifacts
grails clean

# Run tests
grails test-app

# Run a single test
grails test-app ClassName

# Access Grails console for interactive development
grails console
```

### Docker/Container Development
```bash
# Build and run with Docker Compose
docker-compose up --build

# Or with Podman (if using podman-compose)
podman-compose up --build

# The application will be available at:
# - HTTP: http://localhost:8080
# - HTTPS: https://localhost:8443
```

The Docker setup includes:
- Tomcat 9 with Grails 2.5.6 (with SSL/HTTPS support)
- MySQL 9 database

Environment variables required for production (set in `.env` or environment):
- `MYSQL_ROOT_PASSWORD`
- `MYSQL_USER`
- `MYSQL_USER_PASSWORD`

### SSL/HTTPS Configuration

Tomcat is configured with both HTTP (8080) and HTTPS (8443) connectors:
- Custom `server.xml` in `tomcat-config/` directory configures both connectors
- Self-signed SSL certificate is auto-generated during Docker build via `generate-keystore.sh`
- Keystore location: `/usr/local/tomcat/conf/keystore.jks` (password: `changeit`)
- Spring Security port mapper redirects HTTP (8080) to HTTPS (8443)

To use a Let's Encrypt production certificate:
1. See `docs/letsencrypt-setup.md` for complete setup instructions
2. Quick summary:
   - Install certbot on host: `brew install certbot` (macOS)
   - Obtain certificate: `sudo certbot certonly --standalone -d yourdomain.com`
   - Convert to JKS format: Use OpenSSL + keytool (see docs)
   - Mount certificate into container via volume in compose.yaml
   - Remove self-signed cert generation from Dockerfile
   - Set up auto-renewal with cron job

**Note:** To use standard privileged ports (80/443) instead of 8080/8443:
- With Docker Desktop: Simply change the port mappings in `compose.yaml` from `"8080:8080"` and `"8443:8443"` to `"80:8080"` and `"443:8443"`
- With Podman: Privileged port binding requires additional configuration (rootful mode or system-level port forwarding)

### Container Runtime Options

**Docker Desktop (macOS/Windows):**
- Privileged port binding (80/443): Easy - just change port mappings in compose.yaml
- Licensing for 501(c)(3) nonprofits:
  - Free tier: <250 employees AND <$10M annual revenue
  - Nonprofits mentioned in free tier, but size/revenue limits may apply
  - Contact Docker Sales for clarification on 501(c)(3) exemptions
- Download: https://www.docker.com/products/docker-desktop/

**OrbStack (macOS only):**
- Privileged port binding (80/443): Easy - works without admin privileges via macOS networking
- Built-in automatic HTTPS with zero-config domains
- Licensing for 501(c)(3) nonprofits:
  - Generally requires paid license ($8/month normally)
  - 30-day free trial for evaluation
  - May offer free licenses for educational/open-source use (case-by-case)
  - Contact OrbStack for potential nonprofit discounts
- Download: https://orbstack.dev/

**Podman (Cross-platform):**
- Privileged port binding (80/443): Challenging on macOS - requires rootful mode or port forwarding
- Licensing: Completely free and open source, no restrictions
- Best for: Organizations that want to avoid licensing concerns or can work with non-privileged ports

## Architecture

### Core Domain Model

**Client Management Hierarchy:**
- `Client` - Main entity representing a client. References `Person` for demographics and has relationships to cases, notes, appointments, service records, sponsors, and conflicts
- `Person` - Demographic information (name, DOB, phone, gender, race, English proficiency). Has one `Address` and one `BirthPlace` (both eager-fetched)
- `ClientCase` (also called "Intake") - Legal case records with intake type (STAFF_ADVISE or STAFF_REPRESENTATION), case type (immigration form), result, dates, and attorney assignment

**Important Relationships:**
- Client → Person (many-to-one)
- Client → ClientCase (one-to-many)
- Client → Note, Appointment, ServiceRecord, Conflict (one-to-many)
- Client ↔ Sponsor (many-to-many via ClientSponsorRelation)
- Person → Address, BirthPlace (one-to-one, eager-fetched to prevent N+1 queries)

**Status Tracking:**
The Client domain has extensive methods for tracking immigration status achievements:
- 40+ status types defined in `StatusAchieved` enum (Citizenship, DACA, LPR, TPS, etc.)
- Each status has `has*Achieved()` and `has*Attempted()` methods
- Statuses tracked via successful case results with `associatedStatus`

### Services

**ClientService** (`grails-app/services/ClientService.groovy`):
- `filterStatus()` - Core filtering logic for clients based on intake type, status achievement, and date ranges
- `intakeTypeCounts()` - Aggregates staff advise vs representation statistics
- Uses Joda-Time `Interval` for date range analysis
- Contains `statusFuncs` static map with 25+ status-specific predicate closures

### Controllers and Key Operations

**ClientController** (`grails-app/controllers/ClientController.groovy`):
- `search()` - Advanced multi-field search with special prefixes:
  - `city:`, `county:`, `state:`, `birth country:` for location filtering
  - Full-text search across client name, address, phone, and notes
  - Service date range filtering
- `report()` - Statistical reporting with **heavy performance optimization**:
  - Uses `@Cacheable` with reportCache (1-hour TTL)
  - **GParsPool parallel processing** for filtering large client sets (chunks of 100)
  - Filters by municipality, attorney, home country, date ranges
  - Returns intake type counts and statistics
- Standard CRUD operations (list, show, create, save, edit, update, delete)

**Other Controllers:**
- `ClientCaseController` - Manages case/intake records
- `NoteController` - Notes on clients and cases
- `AppointmentController`, `ServiceRecordController` - Scheduling and time tracking
- `LoginController`, `LogoutController` - Authentication (delegates to Spring Security)

### Security Model

Uses Spring Security Core plugin with role hierarchy:
```
ROLE_ADMIN > ROLE_ATTORNEY > ROLE_STAFF > ROLE_VOLUNTEER > ROLE_INTERN
```

- Authentication: bcrypt password hashing
- Controllers use `@Secured` annotations (e.g., `@Secured(['IS_AUTHENTICATED_FULLY'])`)
- Default admin user created in BootStrap.groovy (username: `admin`, password: `admin`, marked `passwordExpired: true`)
- Public paths: `/`, `/register/**`, `/index`, static assets
- URL-based security rules in Config.groovy with `rejectPublicInvocations: true`

### Performance Optimizations

**Query Caching:**
- Ehcache configuration in Config.groovy with `reportCache` (1-hour TTL)
- Hibernate second-level cache and query cache enabled
- Domain classes marked with `cache: true`
- `@Cacheable` decorator on `ClientController._report()` method

**Parallel Processing:**
- `GParsPool` library used in report generation
- Client filtering done in parallel chunks of 100 records
- See ClientController.groovy:~800 for implementation

**Eager Fetching:**
- `Person.address` and `Person.placeOfBirth` use eager fetch to prevent N+1 queries
- Configured in domain class mappings

**Custom Sorting:**
- `Person` implements `Comparable` with sortable name handling
- Handles name prefixes (DE, DEL, DE LA) correctly
- `ClientComparator` utility for list sorting

### Date Handling

Uses **Joda-Time** library extensively:
- `DateTimeFormatter` for parsing and formatting
- `Interval` class for date range operations
- Date format patterns:
  - Display: "MMM-dd-yyyy"
  - Forms: "dd-MM-yyyy HH:mm"
  - jQuery picker: "dd-mm-yy"

### Database Configuration

**Development:** HSQLDB (in-memory, default)
**Production:** MySQL 8.0.33

Production configuration reads from environment variables (DataSource.groovy):
- `MYSQL_HOST` - Database host
- `MYSQL_DATABASE` - Database name
- `MYSQL_USER` - Database user
- `MYSQL_USER_PASSWORD` - Database password

Connection URL: `jdbc:mysql://${MYSQL_HOST}:3306/${MYSQL_DATABASE}`

## Common Development Patterns

### Creating New Domain Classes
Follow existing patterns:
- Add `cache true` in mapping for cacheable entities
- Use `belongsTo` for cascade delete behavior
- Define `toString()` for readable object representation
- Consider eager fetching for frequently accessed relationships

### Adding Controller Actions
- Use `@Secured` annotations for access control
- Follow Grails convention: list, show, create, save, edit, update, delete
- Use `@Cacheable` for expensive read operations
- Consider GParsPool for operations over large datasets

### Search/Filter Implementation
See `ClientController.search()` for the pattern:
- Use HQL with `executeQuery()` for complex queries
- Support special prefix syntax for structured filtering
- Implement pagination via `max` and `offset` parameters

### Working with Status Achievements
Status tracking uses two mechanisms:
1. `CaseResult.associatedStatus` - Links a case result to a status
2. Client methods - `has*Achieved()` checks for successful results, `has*Attempted()` checks for any result

To add a new status:
1. Add enum value to `StatusAchieved`
2. Add methods to `Client` domain
3. Update `ClientService.statusFuncs` map
4. Add to UI selectors if needed

## Testing Notes

Bootstrap.groovy creates test data:
- All role types (ADMIN, ATTORNEY, STAFF, VOLUNTEER, INTERN)
- Default admin user (credentials: admin/admin)
- Initializes Address and BirthPlace caches

## Key Dependencies

- Grails 2.5.6
- Spring Security Core 2.0-RC5
- GPars 1.2.1 (parallel processing)
- Joda-Time (date manipulation)
- jQuery 1.11.1 + jQuery-UI 1.10.4
- MySQL Connector 8.0.33
- Tomcat 8.0.22 (build plugin)

## Email Configuration

Configured for Gmail SMTP in Config.groovy:
- Host: smtp.gmail.com:465
- Uses SSL/TLS authentication
- Default sender: admin@boulderayuda.org
- Note: Email credentials are in Config.groovy (should be externalized)
