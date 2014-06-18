<head>
    <meta name='layout' content='main'/>
    <title>Create or Link Account</title>
</head>
<body>
		<div class="container">
			<div class="row">
			   <h4><g:message code="springSecurity.oauth.registration.link.not.exists" default="No user was found with this account." args="[session.springSecurityOAuthToken.providerName]"/></h4>
			</div>
		</div>
		<g:render template="/user/registrationForm" />
		<div class="container" ng-controller="UserCtrl" ng-init="initUserRegistrationView()">
			<div class="row">
				<div class="col-lg-8 col-lg-offset-2">
					<div cloak class="well">
						<form class="form-horizontal" id="linkForm" ng-submit="register(user)">
							<h2>Fill the following information to link your account</h2>
							<br/>
							<fieldset>
								<div class="form-group" ng-class="{true: 'has-error', false: ''}[errors['email'] != undefined]">
									<label for="email" class="col-lg-2 control-label">Email</label>
									<div class="col-lg-10">
										<input rel='popover' data-toggle="popover" data-placement="top" data-content="{{errors.email.message}}" id='email' type="text" class="form-control" ng-model="user.email">
									</div>
								</div>
								<div class="form-group" ng-class="{true: 'has-error', false: ''}[errors['password'] != undefined]">
									<label for="password" class="col-lg-2 control-label">Password</label>
									<div class="col-lg-10">
										<input rel='popover' data-toggle="popover" data-placement="top" data-content="{{errors.password.message}}" id='password' type="password" class="form-control" ng-model="user.password">
									</div>
								</div>
								<div class="text-center">
									<button type="submit" class="btn btn-lg btn-success lead">
										Link
									</button>
								</div>
							</fieldset>
						</form>
					</div>
				</div>
			</div>
			<div class="row">
			    <g:link controller="login" action="auth"><g:message code="springSecurity.oauth.registration.back" default="Back to login page"/></g:link>
			</div>
		</div>
		


	<script type='text/javascript'>
	    $(function () {
	      $("[rel='popover']").popover();
	  	});
   	</script>
</body>
