var app = angular.module('webApp');

app.factory('KlijentService', function klijentService($http) {
	
	klijentService.regKlijenta = function(klijent, id){
		return $http.post("/zaposleni/registracijaKlijenta/"+id, klijent);
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
	
	klijentService.izbrisiKlijenta = function(id){
		return $http.put("/klijent/izbrisiKlijenta/" + id);
	}
	
	klijentService.izmjeniKlijenta = function(klijent, id){
		return $http.put("/klijent/izmjeniKlijenta/" + id, klijent);
	}
	
	return klijentService;
});