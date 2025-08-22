<html>
    <head>
        <title>ilcDB System Diagnostics</title>
	<meta name="layout" content="main" />
    </head>
    <body>
	<div class="nav">
	    <g:navLinks />
	</div>
        <h1 style="margin-left:20px;">ilcDB System Diagnostics</h1>
	<br />
	<br />
        <div class="dialog" style="margin-left:20px;width:60%;">
	    <h3 style="margin-left:20px;">Main</h3>
	    <br />
	    <ul>
		<li class="controller"><g:link controller="person">People</g:link></li>
		<li class="controller"><g:link controller="client">Clients</g:link></li>
		<ul>
		    <li class="controller"><g:link controller="note">Notes</g:link></li>
		    <li class="controller"><g:link controller="clientCase">Cases</g:link></li>
		    <li class="controller"><g:link controller="appointment">Appointments</g:link></li>
		</ul>
		<li class="controller"><g:link controller="sponsor">Sponsors</g:link></li>
		<li class="controller"><g:link controller="clientSponsorRelation">Client-sponsor relations</g:link></li>
	    </ul>
	</div>
	<br />
	<br />
        <div class="dialog" style="margin-left:20px;width:60%;">
	    <h3 style="margin-left:20px;">System Controls</h3>
	    <ul>
		<li class="controller"><g:link controller="databaseUser">Users</g:link></li>
		<li class="controller"><g:link controller="role">Roles</g:link></li>
		<li class="controller"><g:link controller="requestmap">Site Security</g:link></li>
	    </ul>
	</div
	<br />
	<br />
        <div class="dialog" style="margin-left:20px;width:60%;">
	    <h3 style="margin-left:20px;">Authentication Controllers</h3>
	    <ul>
		<li class="controller"><g:link controller="login">Login</g:link></li>
		<li class="controller"><g:link controller="logout">Logout</g:link></li>
	    </ul>
	</div
	<br />
	<br />
        <div class="dialog" style="margin-left:20px;width:60%;">
	    <h3>All available controllers</h3>
            <ul>
              <g:each var="c" in="${grailsApplication.controllerClasses}">
                    <li class="controller"><g:link controller="${c.logicalPropertyName}">${c.fullName}</g:link></li>
              </g:each>
            </ul>
        </div>
	<br />
	<br />
        <div class="dialog" style="margin-left:20px;width:60%;">
	    <h3>Request Parameters (${request.getClass()})</h3>
	    <table>
		<thead>
		    <tr>
			<th>Request key</th>
			<th>Request value</th>
			<th>Request value type</th>
		    </tr>
		</thead>
		<tbody>
		    <g:each var="r" in="${request}">
		    <tr>
			<td>${r.key}</td>
			<td>${r.value}</td>
			<td>${r.value?.getClass()}</td>
		    </tr>
		    </g:each>
		</tbody>
	    </table>
	</div>
	<br />
	<br />
        <div class="dialog" style="margin-left:20px;width:60%;">
	    <h3>Response Parameters</h3>
	    <table>
		<thead>
		    <tr>
			<th>Response key</th>
			<th>Response value</th>
		    </tr>
		</thead>
		<tbody>
		    <g:each var="r" in="${response}">
		    <tr>
			<td>${r}</td>
		    </tr>
		    </g:each>
		</tbody>
	    </table>
	</div>
	<br />
	<br />
        <div class="dialog" style="margin-left:20px;width:60%;">
	    <h3>Session Parameters (${session.getClass()})</h3>
	    <table>
		<thead>
		    <tr>
			<th>Session key</th>
			<th>Session value</th>
			<th>Session value class</th>
		    </tr>
		</thead>
		<tbody>
		    <g:each var="s" in="${session}">
		    <% for( Enumeration e = session.getAttributeNames(); e.hasMoreElements(); ) { %>
		    <g:set var="attribute" value="${e.nextElement()}" />
		    <tr>
			<td>${attribute}</td>
			<td>${session.getAttribute(attribute)}</td>
			<td>${session.getAttribute(attribute).getClass()}</td>
		    </tr>
		    <% } %>
		    </g:each>
		</tbody>
	    </table>
	</div>
	<br />
	<br />
        <div class="dialog" style="margin-left:20px;width:60%;">
	    <h3>Flash map (${flash.size()} keys)</h3>
	    <table>
		<thead>
		    <tr>
			<th>Flash key</th>
			<th>Flash value</th>
		    </tr>
		</thead>
		<tbody>
		    <g:each var="f" in="${flash}">
		    <tr>
			<td>${f.key}</td>
			<td>${f.value}</td>
		    </tr>
		    </g:each>
		</tbody>
	    </table>
	</div>
	<br />
	<br />
        <div class="dialog" style="margin-left:20px;width:60%;">
	    <h3>Authentication information</h3>
	    <table>
		<thead>
		    <tr>
			<th>Key</th>
			<th>Value</th>
		    </tr>
		</thead>
		<tbody>
		    <tr>
			<td>Logged in</td>
			<g:isLoggedIn><td>true</td></g:isLoggedIn>
			<g:isNotLoggedIn><td>false</td></g:isNotLoggedIn>
		    </tr>

		    <tr>
			<td>Username</td>
			<td><g:loggedInUserInfo field="userRealName"/></td>
		    </tr>
		</tbody>
	    </table>
	</div>
	<br />
	<br />

    </body>
</html>
