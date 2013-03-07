<html>
    <head>
        <title><g:layoutTitle default="Immigrant Legal Center" /></title>
        <link rel="stylesheet" href="${createLinkTo(dir:'css',file:'main.css')}" />
        <link rel="shortcut icon" href="${createLinkTo(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
        <g:layoutHead />
        <g:javascript library="application" />
    </head>
    <body>
      <div class="top">
        <div id="spinner" class="spinner" style="display:none;">
            <img src="${createLinkTo(dir:'images',file:'spinner.gif')}" alt="Spinner" />
        </div>
        <div class="logo"><img src="${createLinkTo(dir:'images',file:'scales.jpg')}" alt="Immigrant Legal Center" /></div>
        <div class="nav">
            <g:navLinks clientid="${client?.id}">
                <span class="menuButton"><g:link controller="client" class="search" action="search">Search Clients</g:link></span>
                <span class="menuButton"><g:link controller="client" class="create" action="create">New Client</g:link></span>
                <span class="menuButton"><g:link controller="client" class="report" action="report">Reports</g:link></span>
            </g:navLinks>
        </div>
      </div>
        <g:layoutBody />
    </body>
</html>
