var app = angular.module('webApp');

app.factory('drzavaService', function drzavaService($http) {
	
	drzavaService.preuzmiDrzavu = function(){
		return $http.get("/drzava/sveDrzave");
	}
	
	drzavaService.preuzmiDrzaveValute = function(id){
		return $http.get("/drzava/sveDrzaveValute/" + id);
	}
	
	drzavaService.sveValute = function(){
		return $http.get("/valuta/sveValute");
	}
	
	drzavaService.pretraziDrzave = function(drzava, idValute){
		return $http.put("/drzava/pretraziDrzave/" + idValute, drzava);
	}
	
	drzavaService.izmeniDrzavu = function(drzava, id){
		return $http.put("/drzava/izmeniDrzavu/" + id, drzava);
	}
	
	drzavaService.regDrzavu = function(drzava, id){
		return $http.post("/drzava/registracijaDrzave/"+id, drzava);
	}
	
	drzavaService.izbrisiDrzavu = function(id){
		return $http.put("/drzava/izbrisiDrzavu/" + id);
	}
	return drzavaService;
});