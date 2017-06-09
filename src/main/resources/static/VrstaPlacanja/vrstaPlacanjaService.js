var app = angular.module('webApp');

app.factory('VrstaPlacanjaService', function vrstaPlacanjaService($http) {
	
	vrstaPlacanjaService.regVrstaPlacanja = function(vrstaPlacanja){
		return $http.post("/vrstaPlacanja/registracijaVrstePlacanja", vrstaPlacanja);
	}
	
	vrstaPlacanjaService.sveVrstePlacanja = function(){
		return $http.get("/vrstaPlacanja/sveVrstePlacanja");
	}
	
	vrstaPlacanjaService.izbrisiVrstuPlacanja = function(id){
		return $http.put("/vrstaPlacanja/izbrisiVrstuPlacanja/" + id);
	}
	
	vrstaPlacanjaService.izmjeniVrstuPlacanja = function(vrstaPlacanja){
		return $http.put("/vrstaPlacanja/izmjeniVrstuPlacanja", vrstaPlacanja);
	}
	
	return vrstaPlacanjaService;
});