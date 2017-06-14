var app = angular.module('webApp');

app.factory('dsrService', function dsrService($http) {
	
	
	dsrService.svaDSRacuna = function(id){
		return $http.get("/dnevnoStanjeRacuna/svaDnevnaStanjeRacunaDatog/" + id);
	}
	
	dsrService.pretraziDSR = function(dsr,id){
		return $http.get("/dnevnoStanjeRacuna/pretraziDnevnaStanjeRacuna/" + id, dsr)
	}
	
	dsrService.sviRacuni = function(id){
		return $http.get("/racun/sviRacuni");
	}
	
	return dsrService;
});