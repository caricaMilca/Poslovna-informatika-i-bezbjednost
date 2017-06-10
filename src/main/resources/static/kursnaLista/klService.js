var app = angular.module('webApp');

app.factory('KLService', function klService($http) {
	
	klService.preuzmiNM = function(){
		return $http.get("/kursnaLista/sveKursneListe");
	}
	
	klService.pretraziNM = function(nm, idDrzave){
		return $http.put("/kursnaLista/pretraziKursneListe" , nm);
	}
	
	klService.izmeniNM = function(nm, id){
		return $http.put("/kursnaLista/izmeniKursnuListu", nm);
	}
	
	klService.regNM = function(nm, id){
		return $http.post("/kursnaLista/dodavanjeKursneListe", nm);
	}
	
	klService.izbrisiNM = function(id){
		return $http.put("/kursnaLista/izbrisiKursnuListu/" + id);
	}
	return klService;
});