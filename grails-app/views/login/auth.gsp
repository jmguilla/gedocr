<!DOCTYPE html>
<html>
	<head>
	<meta name='layout' content='main' />
	
	<title><g:message code="springSecurity.login.title" /></title>
	
	<r:require module="application" />
	
	</head>

	<body>
	
		<div class="container">
			
			<sec:ifNotLoggedIn roles="ROLE_USER">
				<div class="row">
				
					<div class="col-lg-6 col-lg-offset-3">
						
						<g:render template="/shared/alerts" />
						
						<div class="well">
		
							<form class="form-horizontal" id="loginForm"
								action='${request.contextPath}/j_spring_security_check'
								method='POST'>
								<fieldset>
									<!-- NOT logged -->
									<div class="form-group">
										<label for="username" class="col-lg-2 control-label">Email</label>
										<div class="col-lg-10">
											<input id='username' name='j_username' type="text"
													placeholder="Email" class="form-control">
										</div>
									</div>
									<div class="form-group">
										<label for="password" class="col-lg-2 control-label">Password</label>
										<div class="col-lg-10">
											<input id='password' name='j_password' type="password"
													placeholder="Password" class="form-control">
										</div>
									</div>
									<div class="text-center">
										<button type="submit" id="submit" class="btn btn-lg btn-success">
											${message(code: "springSecurity.login.button")}
										</button>
									</div>
								</fieldset>
							</form>
						</div>
						
						
						<div class="well text-center" >
							<facebookAuth:connect/>
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
