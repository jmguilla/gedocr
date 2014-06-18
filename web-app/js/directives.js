/* Directives */

var directives = angular.module('directives', []);

/**
 * Directive affectant les donn�es "chart" pr�sentes dans le scope
 */
directives.directive('sdyChart', function() {
	return {
		restrict : 'A',
		link : function($scope, elm, attrs) {
			$scope.$watch('chart', function() {
				var chart = new google.visualization.LineChart(elm[0]);
				chart.draw($scope.chart.data, $scope.chart.options);
			}, true);
		}
	};
});
