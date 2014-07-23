// load google charts api
google.load('visualization', '1', {
	packages : [ 'corechart' ]
});
// google.setOnLoadCallback(function() {
// angular.bootstrap(document.body, ['app']);
// });

var controllers = angular.module("controllers", []);

// Main controller
controllers.controller("MainCtrl",
		function($scope, $rootScope, Alert, WebSite) {

			$rootScope.mainInit = function() {
				WebSite.setTitle("mywebsite");
			};

			$rootScope.WebSite = WebSite;
			// store alerts in a single place, the $rootScope, accessed by service
			// Alert
			$rootScope.alerts = [];
			$rootScope.notifications = [];
			$rootScope.alertTopDisplay = true;

		});

controllers.controller("NavCtrl", function($scope, $window) {
	$scope.upload = function(uploadPath) {
		$window.location.href = uploadPath;
	}
});

controllers.controller("UserCtrl", function($scope, $modal, User, Alert, Notification) {

	$scope.initUserView = function() {
		Notification.query({actionId: 'query', c: 'UserController'},function(data, headers){
			angular.forEach(data, function(notification, index){
				this.push(notification);
			}, $scope.notifications);
		});
		$scope.getUser();
	}

	$scope.initUserRegistrationView = function() {
		$scope.user = {};
		$scope.user.errors = {};
	}

	$scope.initAccountLinkView = function() {
		$scope.initUserRegistrationView();
		$scope.command = {};
		$scope.command.errors = {};
	}

	$scope.initUserEditView = function() {
		$scope.getUser();
		$scope.initAccountLinkView();
	}
	
	$scope.initAccountsView = function(){
		$scope.getUser({serialize: 'withOAuthIDs'});
	}

	/**
	 * Get user and inject in scope
	 */
	$scope.getUser = function(params) {
		User.getUser(params, function(data, headers) {
			$scope.user = data.user;
		}, function(httpResponse) {
			Alert.addAlert({
				type : httpResponse.data.alert,
				content : httpResponse.data.message
			});
		});
	}

	$scope.register = function(user) {
		User.register(user, function(data, headers) {
			// redirect to main page
			Alert.addAlert(data, -1);
			$scope.userCreated = true;
		}, function(httpResponse) {
			Alert.addAlert(httpResponse.data);
			$scope.user.errors = {}
			$scope.user.errors = Alert.populateErrors(httpResponse.data.user.errors);
		});
	}

	$scope.linkAccount = function(command) {
		User.linkAccount(command, function(data, headers) {
			Alert.addAlert(data, -1);
			$scope.userCreated = true;
		}, function(httpResponse) {
			Alert.addAlert(httpResponse.data);
			$scope.command.errors = {}
			$scope.command.errors = Alert
					.populateErrors(httpResponse.data.command.errors);
		});
	}

	$scope.updatePWD = function() {
		User.updatePWD($scope.command, function(data, headers) {
			// reset fields
			$scope.command = {};
			Alert.addAlert(data);
		}, function(httpResponse) {
			$scope.command.errors = {}
			$scope.command.errors = Alert
					.populateErrors(httpResponse.data.command.errors);
			Alert.addAlert(httpResponse.data);
		});
	}

	$scope.update = function() {
		User.update($scope.user, function(data, headers) {
			// reset fields
			$scope.user = data.user;
			Alert.addAlert(data);
		}, function(httpResponse) {
			$scope.user.errors = {}
			$scope.user.errors = Alert
					.populateErrors(httpResponse.data.command.errors);
			Alert.addAlert(httpResponse.data);
		});
	}
});

controllers.controller("UploadCtrl", function($scope, INode, Alert) {
	$scope.init = function() {
		$scope.selectedDirectory = undefined;
		$scope.selectedDirectories = [];
		$scope.selectSize = 5;
		$scope.directories = undefined;
		INode.directories(function(data, header){
			$scope.directories = data.result;
			$scope.directoriesForSelection = data.result;
		},function(httpResponse){
			$scope.command.errors = Alert.populateErrors(httpResponse.data.command.errors);
		});
	}
	
	$scope.directorySelectedChanged = function(index){
		if(index != undefined){
			if(index == -1){
				// rollback to root
				$scope.selectedDirectories =[];
				$scope.selectedDirectory = undefined;
				$scope.directoriesForSelection = $scope.directories;
			}else{
				// rollback to a parent directory
				$scope.selectedDirectory = $scope.selectedDirectories[index];
				$scope.selectedDirectories.splice(index, $scope.selectedDirectories.length - index);
			}
		}
		// selected among the different options
		if($scope.selectedDirectory != undefined && $scope.selectedDirectory.children != undefined){
			$scope.directoriesForSelection = $scope.selectedDirectory.children;
			$scope.selectedDirectories.push($scope.selectedDirectory);
		}
	}
});
