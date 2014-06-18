<!DOCTYPE html>
<html>
	<head>
	<meta name='layout' content='main' />
	<title><g:message code="gedocr - Register" /></title>
	<r:require module="application" />
	</head>
	<body>
		<g:render template="/user/registrationForm" />

		<script type='text/javascript'>
		    $(function () {
		      $("[rel='popover']").popover();
		  	});
    	</script>
	</body>
</html>
