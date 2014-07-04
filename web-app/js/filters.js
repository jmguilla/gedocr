angular.module('gedocr.filters', []).filter('fileFilter', function(){
	return function(files, filterString) {
		var result = files;
		if(!angular.isUndefined(files) && !angular.isUndefined(filterString) && filterString.length >= 3){
			result = []
			var secondChance = [];
			filterString = filterString.toLowerCase();
			var filterStrings = filterString.split(" ");
			// first step, only testing then entire filter string, without splitting
			// by whitespace
			angular.forEach(files, function(file){
				var added = false;
				if(file.paths[0].toLowerCase().indexOf(filterString) >= 0){
					added = true;
					result.push(file);
				}else if(!angular.isUndefined(file.tags) && file.tags.length > 0){
					angular.forEach(file.tags, function(tag){
						if(!added && tag != '' && tag.toLowerCase().indexOf(filterString) >= 0){
							added = true;
							result.push(file);
						}
					});
				}else{
					if(!added){
						secondChance.push(file);
					}
				}
			});
			
			// second step, attempting a match on the remaining entries, after
			// splitting the filter string by whitespace.
			for(iIndex = 0; iIndex < secondChance.length; iIndex++){
				var file = secondChance[iIndex];
				for(jIndex = 0; jIndex < filterStrings.length; jIndex++){
					if(file.paths[0].toLowerCase().indexOf(filterStrings[jIndex]) >= 0){
						result.push(file);
						break;
					}else if(!angular.isUndefined(file.tags) && file.tags.length > 0){
						for(kIndex = 0; kIndex < file.tags.length; kIndex++){
							if(file.tags[kIndex].toLowerCase().indexOf(filterString) >= 0){
								result.push(file);
								break;
							}
						}
					}
				}
			}
		}
		return result;
	};
});
