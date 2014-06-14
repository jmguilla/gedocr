<!DOCTYPE html>
<html>
	<head>
	<meta name='layout' content='main' />
	
	<title><g:message code="fr - Register" /></title>
	
	<r:require module="application" />
	
	</head>

	<body>
	
		<div class="container" ng-controller="UserCtrl">
			<sec:ifNotLoggedIn roles="ROLE_USER">
				<div class="row">
				
					<div class="col-lg-8 col-lg-offset-2">
						<g:render template="/shared/alerts" />
						
						<div class="well">
		
							<form class="form-horizontal" id="loginForm"
								 ng-submit="register(email,password)">
								<h2>Fill the following informations to register</h2>
								<br/>
								<fieldset>
									<div class="form-group">
										<label for="username" class="col-lg-2 control-label">User name</label>
										<div class="col-lg-10">
											<input id='username' type="text" class="form-control"
													ng-model="username">
										</div>
									</div>
									<div class="form-group">
										<label for="email" class="col-lg-2 control-label">Email</label>
										<div class="col-lg-10">
											<input id='email' type="text" class="form-control"
													ng-model="email">
										</div>
									</div>
									<div class="form-group">
										<label for="confirmation-email" class="col-lg-2 control-label"></label>
										<div class="col-lg-10">
											<input id='confirmation-email' type="text"
													placeholder="Confirm Email..." class="form-control"
													ng-model="emailConfirmation">
										</div>
									</div>
									<div class="form-group">
										<label for="password" class="col-lg-2 control-label">Password</label>
										<div class="col-lg-10">
											<input id='password' type="password" class="form-control"
													ng-model="password">
										</div>
									</div>
									<div class="form-group">
										<label for="confirmation-password" class="col-lg-2 control-label"></label>
										<div class="col-lg-10">
											<input id='confirmation-password' type="password"
													placeholder="Confirm password..." class="form-control"
													ng-model="passwordConfirmation">
										</div>
									</div>
									<div class="text-center">
										<button type="submit" class="btn btn-lg btn-success lead">
											Register
										</button>
									</div>
								</fieldset>
							</form>
						</div>
					</div>
				</div>
			</sec:ifNotLoggedIn>
		
			<sec:ifLoggedIn>
					You are logged... what the f*** are you doin here !?
			</sec:ifLoggedIn>
		</div>
	
		<script type='text/javascript'>
		<!--
			(function() {
				document.forms['loginForm'].elements['username'].focus();
			})();
		// -->
		</script>
	</body>
</html>
