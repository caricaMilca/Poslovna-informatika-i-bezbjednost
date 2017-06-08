var app = angular.module('webApp');

app.factory('NMService', function nmService($http) {
	
	nmService.preuzmiNM = function(){
		return $http.get("/naseljenoMjesto/svaNaseljenaMjesta");
	}
	
	nmService.sveDrzave = function(){
		return $http.get("/drzava/sveDrzave");
	}
	
	nmService.pretraziNM = function(nm, idDrzave){
		return $http.put("/naseljenoMjesto/pretraziNaseljenaMjesta/" + idDrzave, nm);
	}
	
	nmService.izmeniNM = function(nm, id){
		return $http.put("/naseljenoMjesto/izmjeniNaseljenoMjesto/" + id, nm);
	}
	
	nmService.regNM = function(nm, id){
		return $http.post("/naseljenoMjesto/registracijaNaseljenogMjesta/"+id, nm);
	}
	
	nmService.izbrisiNM = function(id){
		return $http.put("/naseljenoMjesto/izbrisiNaseljenoMjesto/" + id);
	}
	return nmService;
});