*<!DOCTYPE html>
<html>
	<head>
		<meta name='layout' content='main' />
		<r:require module="application" />
		<link rel="stylesheet" href="/css/upload.css">
	</head>
	<body>
		<form class="form-horizontal" role="form" enctype="multipart/form-data" action="${createLink(controller: 'INode', action: 'upload')}" method="post" ng-controller="UploadCtrl" ng-init="init()">
		  <div class="form-group">
		    <label for="document" class="col-sm-2 control-label">Document</label>
		    <div class="col-sm-9 input-group">
		      <input name="document" type="file" class="form-control" id="document" placeholder="document" />
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="search-result" class="col-sm-2 control-label">Target Folder</label>
		    <div class="input-group col-sm-9">
		    	<span class="input-group-addon">
		    	<a href="#" ng-click="directorySelectedChanged(-1)">&nbsp;&nbsp;/</a>
		    	<a href="#" ng-repeat="directory in selectedDirectories" ng-click="directorySelectedChanged($index)">{{directory.name + "/"}}</a></span>
		      	<select class="form-control" size="5" id="search-result" ng-model="selectedDirectory" ng-options="directory.name for directory in directoriesForSelection|orderBy:'name'" ng-change="directorySelectedChanged()">
		      		<option class="list-group-item" value="" ng-if="directories == undefined">-- loading --</option>
				</select>
		    </div>
		  </div>
		  <div class="form-group">
		    <div class="col-sm-offset-2 col-sm-9">
		      <button type="submit" class="btn btn-default">Upload</button>
		    </div>
		  </div>
		  <input type="hidden" name="selectedDirectory" value="{{selectedDirectory}}"/>
		</form>
	</body>
</html>
