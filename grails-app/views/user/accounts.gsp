<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="account" />
		<r:require module="application" />
	</head>
	<body >
		<div ng-cloak class="well" ng-controller="UserCtrl" ng-init="initAccountsView()">
			<div class="media" ng-repeat="oAuthID in user.oAuthIDs">
			  <a class="pull-left" href="#">
			    <img class="media-object" src="${resource(dir: 'images/providers/')}{{oAuthID.provider}}/logo.jpg" alt="{{oAuthID.provider}}" width="70px" height="70px">
			  </a>
			  <div class="media-body">
			    <h4 class="media-heading">{{oAuthID.provider}}</h4>
			    <div>Your account is currently linked to this provider.</div>
			    <div>Last synchro occurred on 01/01/1900 at 00:00.</div>
			    </div>
			    	<button type="button" class="btn btn-default btn-xs">Unlink</button>
			    	<button type="button" class="btn btn-default btn-xs">Refresh</button>
			    </div>
			  </div>
			</div>
		</div>
	</body>
</html>