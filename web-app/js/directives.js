/* Directives */


var directives = angular.module('directives', []);

/**
 * Directive affectant les données "chart" présentes dans le scope
 */
directives.directive('sdyChart', 
	function () {
		return {
			restrict: 'A',
			link: function($scope, elm, attrs) {
				$scope.$watch('chart', function() {
					var chart = new google.visualization.LineChart(elm[0]);
					chart.draw($scope.chart.data, $scope.chart.options);
				},true);
			}
		};
	}
);
	
