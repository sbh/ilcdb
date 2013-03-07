<html>
    <head>
        <title>ilcDB Home</title>
	<meta name="layout" content="main" />
	<meta http-equiv="refresh" content="1; URL=${g.createLink(controller:'client', action:'list')}">
	<script type="text/javascript">
	<!--
	function Redirect() {
	    location.href = "${g.createLink(controller:'client', action:'list')}";
	}
	// -->
	</script>
    </head>
    <body>
	<br />
	<h3 style="margin-left:5%;"><g:link controller='client' action='list'>Automatically redirecting you to the home page. Click here if you are not redirected.</g:link><h2>
	<script type="text/javascript">
	<!--
	    setTimeout('Redirect()',250);
	// -->
	</script>
	<noscript>
	    <div class="errors" style="margin-left:5%; margin-right:5%;">
		<h3 style="margin-left:20px;">Javascript disabled or is not available</h1>
		<p class="errors" style="margin-left:20px;">
		Javascript is required to use some of the functionality for this application.<br />
		Please upgrade to a current brower or enable javascript.<br />
	    </div>
	</noscript>
    </body>
</html>
