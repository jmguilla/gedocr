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
				<div class="form-group">
					<label for="password" class="col-xs-2 col-md-2 col-lg-2 control-label">Password</label>
					<div class="col-xs-7 col-xs-offset-3 col-md-10 col-md-offset-0 col-lg-10 col-lg-offset-0">
						<div>
						    <button class="btn btn-success" ng-click="openPWDModal()">Change</button>
						</div>
					</div>
				</div>
			</div>
			
			<!-- INFORMATIONS -->
			<div class="well">
				<div class="form-group">
					<label for="email" class="col-lg-2 control-label">Email</label>
					<div class="col-lg-10">
						<input type="email"
							class="form-control" id="email" placeholder=""
							value="{{user.email}}">
					</div>
				</div>
				<div class="form-group">
					<label for="username" class="col-lg-2 control-label">UserName</label>
					<div class="col-lg-10">
						<input type="text"
							class="form-control" id="username" value="{{user.username}}">
					</div>
				</div>
				<div class="form-group">
					<div class="text-center">
						<button type="submit" class="btn btn-success">Save changes</button>
					</div>
				</div>
			</div>
			
		</form>
	</body>

</html>