<!DOCTYPE html>
<html>
	<head>
	<meta name="layout" content="main" />
	
	<title>fr</title>
	
	<r:require module="application" />
	
	</head>
<body>

	<div class="container">

		<g:render template="/shared/alerts" />

		<sec:ifNotLoggedIn>
			<div id="notLoggedInOptions">
				<div class="well text-center">
					<div class="row">
						<div class="col-lg-2 vCenterRow">
							<g:link class="btn btn-success lead" controller="user"
								action="create">Register</g:link>
						</div>
					</div>
				</div>
			</div>
		</sec:ifNotLoggedIn>
						<oauth:connect provider="google" id="google-connect-link">Google</oauth:connect>
						
						Logged with google?
						<s2o:ifLoggedInWith provider="google">yes</s2o:ifLoggedInWith>
						<s2o:ifNotLoggedInWith provider="google">no</s2o:ifNotLoggedInWith>
	</div>

</body>

</html>
