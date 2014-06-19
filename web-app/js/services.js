var services = angular.module("services", [ "ngResource" ]);

services.factory('User', function($resource) {
	return $resource('/user/:actionId/:userId.json', {
		actionId : '',
		userId : '@id'
	}, {
		getUser : {
			method : 'GET',
			params : {
				actionId : 'getUser'
			},
			headers : {
				'Content-Type' : 'application/json',
				'Accept' : 'application/json'
			}
		},

		updatePWD : {
			method : 'POST',
			params : {
				actionId : 'updatePWD'
			},
			headers : {
				'Content-Type' : 'application/json',
				'Accept' : 'application/json'
			}
		},

		register : {
			method : 'POST',
			params : {
				actionId : 'create'
			},
			headers : {
				'Content-Type' : 'application/json',
				'Accept' : 'application/json'
			}
		},
		
		linkAccount: {
			method: 'POST',
			params: {
				actionId: 'linkAccount'
			},
			headers : {
				'Content-Type' : 'application/json',
				'Accept' : 'application/json'
			}
		}
	});
})

services.factory('GrailsNavigation', function() {
	return {
		navigateTo : function(controller, action, id) {
			window.location = "/" + controller + "/" + action + "/" + (id ? id : "");
		}
	};
})

services.factory('Alert', function($rootScope, $timeout) {
	return {
		addAlert : function(alert, timeout) {
			// verify alert coherence
			if (!alert.type) {
				alert.type = "danger"
			}
			if (!alert.content) {
				alert.content = "An error occured."
			}

			$rootScope.alerts.push(alert);

			if (timeout > 0) {
				$timeout(function() {
					$rootScope.alerts.splice($rootScope.alerts.indexOf(alert), 1);
				}, timeout);
			}
		},

		overrideDisplay : function(value) {
			$rootScope.alertTopDisplay = value;
		},

		clear : function() {
			$rootScope.alerts = [];
		},

		populateErrors : function(errors) {
			var outParam = {};
			if (errors && errors.errors) {
				angular.forEach(errors.errors, function(value, key) {
					outParam[value.field] = value;
				}, outParam);
			}
			return outParam;
		}
	};
})

services.factory('WebSite', function($rootScope) {
	var title = 'mywebsite';
	return {
		title : function() {
			return title;
		},

		setTitle : function(newTitle) {
			title = newTitle;
		}
	};
})
