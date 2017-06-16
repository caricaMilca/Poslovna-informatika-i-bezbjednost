var app = angular.module('webApp');

app.factory('RacunService', function racunService($http) {
	
	racunService.regRacuna = function(racun, id, v_id){
		return $http.post("/racun/registracijaRacuna/" + id + "/" + v_id, racun);
	}
	
	racunService.sviRacuniKlijenta = function(id){
		return $http.get("/racun/sviRacuniKlijenta/" + id);
	}
	
	racunService.sviRacuniValute = function(id){
		return $http.get("/racun/sviRacuniValute/" + id);
	}
	
	racunService.sviKlijenti = function(){
		return $http.get("/klijent/sviKlijenti");
	}
	
	racunService.sveValute = function(){
		return $http.get("/valuta/sveValute");
	}
	
	racunService.preuzmiRacune = function(){
		return $http.get("/racun/sviRacuni");
	}
	
	racunService.pretraziRacune = function(racun, id, id_v){
		return $http.put("/racun/pretraziRacune/" + id + "/" + id_v, racun);
	}
	
	racunService.zatvoriRacun = function(racun){
		return $http.put("/racun/zatvoriRacun/", racun);
	}
	
	racunService.kreirajIzvestaj = function(){
		return $http.get("/banka/izvestaj");
	}
	
	return racunService;
});