<g:ifAnyGranted role="ROLE_ROOT,ROLE_ADMINISTRATORS" >
    <span class="menuButton"><span class="controls">System</span></span>
    <span class="menuButton"><g:link class="list" controller="databaseUser">Users</g:link></span>
    <span class="menuButton"><g:link class="list" controller="role">Roles</g:link></span>
    <g:ifAllGranted role="ROLE_ROOT" >
    <span class="menuButton"><g:link class="list" controller="requestmap">Site Security</g:link></span>
    <span class="menuButton"><a href="${createLinkTo(dir:'', file:'diagnostics.gsp')}" class="controls">Diagnostics</a></span>
    </g:ifAllGranted>
</g:ifAnyGranted>
