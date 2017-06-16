var app = angular.module('webApp');

app.factory('medjubankarskiPrenosService', function medjubankarskiPrenosService($http) {
	
	
	medjubankarskiPrenosService.sviMedjubankarskiPrenosi = function(){
		return $http.get("/medjubankarskiPrenos/sviMedjubankarskiPrenosi");
	}

	
	medjubankarskiPrenosService.exportMedjubankarskogPrenosa = function(id){
		return $http.get("/importExportStavkiPlacanja/exportMedjubankarskogPrenosa/" + id);
	}


	
	return medjubankarskiPrenosService;
});