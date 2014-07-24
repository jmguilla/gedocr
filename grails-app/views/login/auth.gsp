<!DOCTYPE html>
<html>
	<head>
	<meta name='layout' content='main' />
	<title><g:message code="springSecurity.login.title" /></title>
	<r:require module="application" />
	<link rel="stylesheet" href="${resource(dir: 'css', file: 'auth.css')}">
	</head>
	<body>
		<div class="container">
			<div class="row">
				<div class="col-lg-6 col-lg-offset-3">
					<div class="well text-center" >
						<oauth:connect provider="google"><p id="google-connect-link"></p></oauth:connect>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>
