<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> 
<html class="no-js" ng-app="app" ng-controller="MainCtrl" ng-init="mainInit()"><!--<![endif]-->
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<g:render template="/shared/headConfig" />
		<g:render template="/shared/analytics" />
		<title><g:layoutTitle default="{{WebSite.title()}}"/></title>
		
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<g:layoutHead/>
		<r:layoutResources />
	</head>
	
	<body>
			
		<!-- facebook share button -->
		<div id="fb-root"></div>
		<script>(function(d, s, id) {
		  var js, fjs = d.getElementsByTagName(s)[0];
		  if (d.getElementById(id)) return;
		  js = d.createElement(s); js.id = id;
		  js.src = "//connect.facebook.net/fr_FR/all.js#xfbml=1&appId=1411642499093079";
		  fjs.parentNode.insertBefore(js, fjs);
		}(document, 'script', 'facebook-jssdk'));</script>
			
		<g:render template="/shared/nav" />
		
		<div class="container">
			
			<div class="row">
				<div id="paramsNav"
						class="col-lg-2">
					<ul class="nav nav-pills nav-stacked">
					  <li class="${(controllerName == 'user' && actionName == 'show' ) ? 'active' : ''}"><g:link
							controller="user" action="show">Resume</g:link>
					  </li>
					  <li class="${(controllerName == 'user' && actionName == 'edit' ) ? 'active' : ''}"><g:link
							controller="user" action="edit">Settings</g:link></li>
					</ul>
				</div>
				<div class="col-lg-10" >

					<g:render template="/shared/alerts"/>
					
					<!-- some space in mobile vertical mode -->
					<div class="hidden-lg">
						<br/>
					</div>
					
					<g:layoutBody/>
					
				</div>
			</div>
		</div>
		
		<g:javascript library="application"/>
		
		<r:layoutResources />
	</body>
</html>
