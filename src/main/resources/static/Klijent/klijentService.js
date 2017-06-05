var app = angular.module('webApp');

app.factory('KlijentService', function klijentService($http) {
	
	klijentService.regKlijentaP = function(klijent, id){
		return $http.post("/zaposleni/registracijaKlijentaPravno/"+id, klijent);
	}
	
	klijentService.regKlijentaF = function(klijent){
		return $http.post("/zaposleni/registracijaKlijentaFizicko", klijent);
	}
	
	klijentService.sveDjelatnosti = function(){
		return $http.get("/djelatnost/sveDjelatnosti");
	}
	
	klijentService.preuzmiKlijente = function(){
		return $http.get("/klijent/sviKlijenti");
	}
	
	klijentService.pretraziKlijente = function(klijent, idDjelatnosti){
		return $http.put("/klijent/pretraziKlijente/" + idDjelatnosti, klijent);
	}
	
	
	return klijentService;
});