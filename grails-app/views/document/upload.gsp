<!DOCTYPE html>
<html>
	<head>
	<meta name='layout' content='main' />
	<r:require module="application" />
	</head>
	<body>
		<form class="form-horizontal" role="form" enctype="multipart/form-data" action="${createLink(controller: 'document', action: 'upload')}" method="post" ng-controller="UploadCtrl" ng-init="init()">
		  <div class="form-group">
		    <label for="document" class="col-sm-2 control-label">Document</label>
		    <div class="col-sm-10">
		      <input name="document" type="file" class="form-control" id="document" placeholder="document" />
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="search" class="col-sm-2 control-label">Target Folder</label>
		    <div class="col-sm-10">
		      <input name="search" type="search" class="form-control" id="search" placeholder="search search for a target folder" ng-model="filter"/>
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="search-result" class="col-sm-2 control-label">Result</label>
		    <div class="col-sm-10">
		      <select size="5" name="search-result" class="form-control" id="search-result">
		      	<option ng-repeat="directory in directories|filter:filter|limitTo: 5" ng-value="directory.path">{{directory.path}}</option>
		      </select>
		    </div>
		  </div>
		  <div class="form-group">
		    <div class="col-sm-offset-2 col-sm-10">
		      <button type="submit" class="btn btn-default">Upload</button>
		    </div>
		  </div>
		</form>
	</body>
</html>
