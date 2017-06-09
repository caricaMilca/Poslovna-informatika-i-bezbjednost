var app = angular.module('webApp');

app.factory('ValuteService', function valuteService($http) {
	
	valuteService.preuzmiV = function(){
		return $http.get("/valuta/sveValute");
	}
	
	
	valuteService.pretraziV = function(v){
		return $http.put("/valuta/pretraziValute", v);
	}
	
	valuteService.izmeniV = function(nm){
		return $http.put("/valuta/izmeniValutu" , nm);
	}
	
	valuteService.regV = function(v){
		return $http.post("/valuta/registracijaValute", v);
	}
	
	valuteService.izbrisiV = function(id){
		return $http.put("/valuta/izbrisiValutu/" + id);
	}
	return valuteService;
});