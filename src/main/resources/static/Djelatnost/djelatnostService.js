var app = angular.module('webApp');

app.factory('DjelatnostService', function djelatnostService($http) {
	
	djelatnostService.regDjelatnost = function(djelatnost){
		return $http.post("/djelatnost/registracijaDjelatnosti", djelatnost);
	}

	
	djelatnostService.sveDjelatnosti = function(){
		return $http.get("/djelatnost/sveDjelatnosti");
	}
	
	djelatnostService.pretraziDjelatnosti = function(djelatnost){
		return $http.put("/djelatnost/pretraziDjelatnosti/", djelatnost);
	}
	
	djelatnostService.izbrisiDjelatnost = function(id){
		return $http.put("/djelatnost/izbrisiDjelatnost/" + id);
	}
	
	djelatnostService.izmeniDjelatnost = function(djelatnost){
		return $http.put("/djelatnost/izmjeniDjelatnost", djelatnost);
	}
	
	return djelatnostService;
});