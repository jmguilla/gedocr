
// load google charts api
google.load('visualization', '1', {packages:['corechart']});
//google.setOnLoadCallback(function() {
//	  angular.bootstrap(document.body, ['app']);
//	});


var controllers = angular.module("controllers", []);

//Main controller
controllers.controller("MainCtrl",
		function($scope, $rootScope, Alert) {
			
			$rootScope.applicationName = "Find a fuckin name";
			
			// store alerts in a single place, the $rootScope, accessed by service Alert
			$rootScope.alerts = [];
			$rootScope.alertTopDisplay = true;
				
		}
);

controllers.controller("UserCtrl",
		function($scope, $modal, User, Alert) {
			
			
			$scope.initUserEditView = function(){
				$scope.getUser();
			}
	
			$scope.initUserView = function(){
				$scope.getUser();
			}
	
			/**
			 * Get user and inject in scope
			 */
			$scope.getUser = function(){
				User.getUser({},
					function(data, headers){
						$scope.user = data.user;
					},
					function(httpResponse){
						Alert.addAlert({type: httpResponse.data.alert, content:httpResponse.data.message});
					});
			}
	
			$scope.register = function(email, password){
				if(email == undefined)
					email = null;
				if(password == undefined)
					password = null;
				User.register({email:email, pwd:password},
					function(data, headers){
						// redirect to main page
						Alert.addAlert({type: data.alert, content:data.message}, -1);
					},
					function(httpResponse){
						Alert.addAlert({type: httpResponse.data.alert, content:httpResponse.data.message});
					}
				);
			}
			
			$scope.updatePWD = function(current, newPWD, newPWDAgain){
				User.updatePWD({current:current, newPWD:newPWD, newPWDAgain:newPWDAgain},
					function(data, headers){
						// reset fields
						$scope.currentPWD = "";
						$scope.newPWD = "";
						$scope.newPWDAgain = "";
						
						if($scope.modalPWD != null){
							$scope.modalPWD.close("ok");
						}

						Alert.addAlert({type: data.alert, content:data.message});
					},
					function(httpResponse){
						Alert.addAlert({type: httpResponse.data.alert, content:httpResponse.data.message});
					});	
			}
			
			$scope.openPWDModal = function () {
				
				Alert.overrideDisplay(false);
				
				var ModalInstanceCtrl = function ($scope, $modalInstance) {
					
					$scope.ok = function (currentPWD, newPWD, newPWDAgain) {
						$scope.updatePWD(currentPWD, newPWD, newPWDAgain)
					};
					
					$scope.cancel = function () {
						$modalInstance.dismiss(false);
					};
				};

				$scope.modalPWD = $modal.open({
					templateUrl: '/partials/user/modal_PWDChange.html',
					controller: ModalInstanceCtrl,
					scope: $scope
				});
				
				$scope.modalPWD.result.then(
						function () {
							Alert.overrideDisplay(true);
						}, 
						function () {
							Alert.overrideDisplay(true);
						});
			};
		}
);

