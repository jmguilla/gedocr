// Main controller
var frApp = angular.module("app", [ "ngRoute", "controllers", "services",
		"filters", "directives", "ui.bootstrap" ]);

/**
 * russian fix (i know...) to avoid angular compilation error : "Controller
 * 'carousel', required by directive 'ngTransclude', can't be found" see :
 * http://stackoverflow.com/questions/20137900/controller-carousel-required-by-directive-ngtransclude-cant-be-found
 * 
 * side effect? who knows !
 */
angular.module('ui.bootstrap.carousel', [ 'ui.bootstrap.transition' ])
		.controller(
				'CarouselController',
				[ '$scope', '$timeout', '$transition', '$q',
						function($scope, $timeout, $transition, $q) {
						} ]).directive('carousel', [ function() {
			return {

			}
		} ]);

/*
 * no use for the moment app.config(["$routeProvider", function($routeProvider){
 * 
 * $routeProvider. when('/', {templateUrl: 'partials/main.html', controller:
 * 'MainCtrl'}). otherwise({redirectTo: '/'}); }
 * 
 * ]);
 */
