<!DOCTYPE html>
<html>
	<head>
	<meta name="layout" content="main" />
	<title>{{WebSite.title()}}</title>
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
		<sec:ifLoggedIn>
			<div class="jumbotron">
			  <h1>Welcome!</h1>
			  <p><a class="btn btn-primary btn-lg" role="button">Welcome!</a></p>
			</div>
		</sec:ifLoggedIn>
		
	</div>

</body>

</html>
