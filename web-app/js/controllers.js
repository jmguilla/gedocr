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
			$rootScope.alertTopDisplay = true;

		});

controllers.controller("UserCtrl", function($scope, $modal, User, Alert) {

	$scope.initUserEditView = function() {
		$scope.getUser();
	}

	$scope.initUserView = function() {
		$scope.getUser();
	}

	$scope.initUserRegistrationView = function() {
		$scope.user = {};
		$scope.user.errors = {};
	}
	
	$scope.initAccountLinkView = function(){
		$scope.initUserRegistrationView();
		$scope.command = {};
		$scope.command.errors = {};
	}

	/**
	 * Get user and inject in scope
	 */
	$scope.getUser = function() {
		User.getUser({}, function(data, headers) {
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

	$scope.updatePWD = function(current, newPWD, newPWDAgain) {
		User.updatePWD({
			current : current,
			newPWD : newPWD,
			newPWDAgain : newPWDAgain
		}, function(data, headers) {
			// reset fields
			$scope.currentPWD = "";
			$scope.newPWD = "";
			$scope.newPWDAgain = "";

			if ($scope.modalPWD != null) {
				$scope.modalPWD.close("ok");
			}

			Alert.addAlert({
				type : data.alert,
				content : data.message
			});
		}, function(httpResponse) {
			Alert.addAlert({
				type : httpResponse.data.alert,
				content : httpResponse.data.message
			});
		});
	}

	$scope.openPWDModal = function() {

		Alert.overrideDisplay(false);

		var ModalInstanceCtrl = function($scope, $modalInstance) {

			$scope.ok = function(currentPWD, newPWD, newPWDAgain) {
				$scope.updatePWD(currentPWD, newPWD, newPWDAgain)
			};

			$scope.cancel = function() {
				$modalInstance.dismiss(false);
			};
		};

		$scope.modalPWD = $modal.open({
			templateUrl : '/partials/user/modal_PWDChange.html',
			controller : ModalInstanceCtrl,
			scope : $scope
		});

		$scope.modalPWD.result.then(function() {
			Alert.overrideDisplay(true);
		}, function() {
			Alert.overrideDisplay(true);
		});
	};
});
