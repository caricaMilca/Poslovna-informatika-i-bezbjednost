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
	
	racunService.preuzmiRacune = function(){
		return $http.get("/racun/sviRacuni");
	}
	
	racunService.pretraziRacune = function(racun, id, id_v){
		return $http.put("/racun/pretraziRacune/" + id + "/" + id_v, racun);
	}
	
	racunService.izbrisiRacun = function(id){
		return $http.put("/racun/izbrisiRacun/" + id);
	}
	
	racunService.zatvoriRacun = function(racun){
		return $http.put("/racun/zatvoriRacun/", racun);
	}
	
	return racunService;
});