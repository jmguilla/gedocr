<!DOCTYPE html>
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
		    <label for="search" class="col-sm-2 control-label">Filter</label>
		    <div class="col-sm-9 input-group">
		      <input name="search" type="search" class="form-control" id="search" placeholder="search for a target folder" ng-model="filter" ng-focus="displaySelectDirectory=true; selectedDirectory = undefined;"/>
		    </div>
		  </div>
		  <div class="form-group" ng-if="displaySelectDirectory">
		    <label for="search-result" class="col-sm-2 control-label">Target Folder</label>
		    <div class="col-sm-9 input-group">
		      	<select class="form-control" size="5" id="search-result" ng-model="$parent.selectedDirectory" ng-options="directory.paths[0] for directory in directories|fileFilter:filter|limitTo:selectSize" ng-change="$parent.displaySelectDirectory=!$parent.displaySelectDirectory; $parent.filter = ''">
		      		<option class="list-group-item" value="" ng-if="$parent.directories == undefined">-- loading --</option>
				</select>
		    </div>
		  </div>
		  <div class="form-group" ng-if="selectedDirectory != undefined">
		    <label for="search-result" class="col-sm-2 control-label">Target Folder</label>
		    <div class="input-group col-sm-9">
		    	<span ng-if="askToCreateDir == false" class="input-group-btn">
                    <button class="btn btn-primary"><i>create!</i></button>
                </span>
		    	<span class="input-group-addon">{{selectedDirectory.paths[0] + "/"}}</span>
		    	<span class="form-control" ng-if="askToCreateDir == true"><a href="#" ng-click="$parent.askToCreateDir = false">+ new directory</a></span>
		    	<input ng-if="askToCreateDir == false" type="text" class="form-control" />
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
