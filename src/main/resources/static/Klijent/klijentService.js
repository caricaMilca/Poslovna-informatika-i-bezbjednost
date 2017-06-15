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
	
	klijentService.exportAnalitikeKlijenta = function(id){
		return $http.get("/importExportStavkiPlacanja/exportAnalitikaKlijenta/" + id);
	}
	
	klijentService.sviKlijentiDjelatnosti = function(id){
		return $http.get("/klijent/sviKlijentiDjelatnosti/" + id);
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