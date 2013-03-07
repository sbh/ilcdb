<sec:ifLoggedIn>
    <sec:access expression="hasRole('ROLE_ADMIN')">
        <span class="menuButton"> <g:link controller="user" action="list" >Users</g:link> | </span>
    </sec:access>
    <span class="menuButton"><g:link controller="logout">sign out</g:link> (<sec:username />)</span>
</sec:ifLoggedIn>

