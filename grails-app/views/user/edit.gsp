<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="account" />
		<r:require module="application" />
	</head>
	<body >
		<form class="form-horizontal" ng-controller="UserCtrl" ng-init="initUserEditView()">
			<!-- PASSWORD -->
			<div class="well">
				<div class="form-group" ng-class="{true: 'has-error', false: ''}[command.errors['password'] != undefined]">
					<label for="currentPassword" class="col-lg-2 control-label">Password</label>
					<div class="col-lg-10">
						<input rel='popover' data-toggle="popover" data-placement="top" data-content="{{command.errors.password.message}}" type="password" class="form-control" id="currentPassword" placeholder="current password" ng-model="command.password" />
					</div>
				</div>
				<div class="form-group" ng-class="{true: 'has-error', false: ''}[command.errors['newPassword'] != undefined]">
					<label for="newPassword" class="col-lg-2 control-label">New Password</label>
					<div class="col-lg-10">
						<input rel='popover' data-toggle="popover" data-placement="top" data-content="{{command.errors.newPassword.message}}" type="password" class="form-control" id="newPassword" placeholder="new password" ng-model="command.newPassword" />
					</div>
				</div>
				<div class="form-group" ng-class="{true: 'has-error', false: ''}[command.errors['newPasswordConfirmation'] != undefined]">
					<label for="newPasswordConfirmation" class="col-lg-2 control-label">Confirmation</label>
					<div class="col-lg-10">
						<input rel='popover' data-toggle="popover" data-placement="top" data-content="{{command.errors.newPasswordConfirmation.message}}" type="password" class="form-control" id="newPasswordConfirmation" placeholder="confirmation" ng-model="command.newPasswordConfirmation" />
					</div>
				</div>
				<div class="form-group">
					<div class="text-center">
						<button type="submit" class="btn btn-success" ng-click="updatePWD()">Update password</button>
					</div>
				</div>
			</div>
			
			<!-- INFORMATIONS -->
			<div class="well">
				<div class="form-group">
					<label for="email" class="col-lg-2 control-label">Email</label>
					<div class="col-lg-10">
						<input type="email" class="form-control" id="email" placeholder="" value="{{user.email}}">
					</div>
				</div>
				<div class="form-group">
					<label for="username" class="col-lg-2 control-label">UserName</label>
					<div class="col-lg-10">
						<input type="text" class="form-control" id="username" value="{{user.username}}">
					</div>
				</div>
				<div class="form-group">
					<div class="text-center">
						<button type="submit" class="btn btn-success">Save changes</button>
					</div>
				</div>
			</div>
			
		</form>
		<script type='text/javascript'>
		    $(function () {
		      $("[rel='popover']").popover();
		  	});
	   	</script>
	</body>
</html>