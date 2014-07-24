<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="account" />
		<r:require module="application" />
	</head>
	<body >
		<form class="form-horizontal" ng-controller="UserCtrl" ng-init="initUserEditView()">
			<!-- INFORMATIONS -->
			<div class="well">
				<div class="form-group">
					<label for="email" class="col-lg-2 control-label">Email</label>
					<div class="col-lg-10">
						<input ng-disabled='true' type="email" class="form-control" id="email" placeholder="" value="{{user.email}}">
					</div>
				</div>
				<div class="form-group">
					<label for="username" class="col-lg-2 control-label">UserName</label>
					<div class="col-lg-10">
						<input type="text" class="form-control" id="username" ng-model="user.username">
					</div>
				</div>
				<div class="form-group">
					<div class="text-center">
						<button type="submit" class="btn btn-success" ng-click='update()'>Save changes</button>
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