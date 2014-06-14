<!-- ALERT AREA -->

<div ng-show="alertTopDisplay"> 
	<!-- flash -->
	<g:if test='${flash.message}'>
		<div class="alert alert-danger fade in">
			<button type="button" class="close" data-dismiss="alert"
				aria-hidden="true">&times;</button>
			<div class="textNoShadow">
				<span> ${flash.message}
				</span>
			</div>
		</div>
	</g:if>
	
	<!-- alerts -->
	<div ng-repeat="alert in alerts"
		class="alert alert-{{alert.type}} alert-dismissable slide" ng-cloak>
		<button ng-click="alerts.splice($index, 1)" type="button"
			class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
		<span ng-bind="alert.content"></span>
	</div>
</div>