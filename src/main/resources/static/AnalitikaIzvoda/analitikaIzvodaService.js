var app = angular.module('webApp');

app.factory('analitikaIzvodaService', function analitikaIzvodaService($http) {
	
	
	analitikaIzvodaService.sveAnalitike = function(){
		return $http.get("/analitikaIzvoda/sveAnalitikeIzvoda");
	}
	
	analitikaIzvodaService.sveAnalitikeValute = function(id){
		return $http.get("/analitikaIzvoda/sveAnalitikeIzvodaValute/" + id);
	}
	
	analitikaIzvodaService.sveValute = function(){
		return $http.get("/valuta/sveValute");
	}
	
	analitikaIzvodaService.sveVrstePlacanja = function(){
		return $http.get("/vrstaPlacanja/sveVrstePlacanja");
	}
	/*
	drzavaService.pretraziAnalitike = function(drzava, idValute, idVrste){
		return $http.put("/drzava/pretraziDrzave/" + idValute, drzava);
	}*/
	
	analitikaIzvodaService.kreiraj = function(nalog){
		return $http.post("/analitikaIzvoda/transakcija/" + nalog.valuta.zvanicnaSifra +"/" + 1, nalog);
	}
	
	return analitikaIzvodaService;
});