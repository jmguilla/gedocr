<!DOCTYPE html>
<html>
	<head>
	<meta name='layout' content='main' />
	<r:require module="application" />
	</head>
	<body>
		<form class="form-horizontal" role="form" enctype="multipart/form-data" action="${createLink(controller: 'document', action: 'upload')}" method="post">
		  <div class="form-group">
		    <label for="document" class="col-sm-2 control-label">Document</label>
		    <div class="col-sm-10">
		      <input name="document" type="file" class="form-control" id="document" placeholder="document">
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
