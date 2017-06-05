var app = angular.module('webApp');

app.factory('SessionService', function sessionService($http) {
	sessionService.logIn = function(logovanje) {
		return $http.get("/korisnik/logovanje/"+logovanje.korisnickoIme+"/"+logovanje.lozinka);
	}

	sessionService.changePassword = function(lozinka){
		return $http.put("korisnik/promenaLozinke/"+lozinka);
	}
	
	sessionService.logout = function(){
		return $http.get("/korisnik/logout");
	}
	
	sessionService.sveDjelatnosti = function(){
		return $http.get("/djelatnost/sveDjelatnosti");
	}
	
	sessionService.regSalterusu = function(salterusa){
		return $http.post("/zaposleni/registracijaSalteruse", salterusa);
	}
	
	sessionService.regKlijentaP = function(klijent, id){
		return $http.post("/zaposleni/registracijaKlijentaPravno/"+id, klijent);
	}
	
	sessionService.regKlijentaF = function(klijent){
		return $http.post("/zaposleni/registracijaKlijentaFizicko", klijent);
	}
	
	sessionService.preuzmiKlijenta = function() {
		return $http.get("/klijent/preuzmiKlijenta/");
	}
	
	sessionService.preuzmiZaposlenog = function() {
		return $http.get("/zaposleni/preuzmiZaposlenog/");
	}
	
	sessionService.preuzmiKlijente = function(){
		return $http.get("/klijent/sviKlijenti");
	}
	
	return sessionService;
	
});