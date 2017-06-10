var app = angular.module('webApp');

app.factory('KvService', function kvService($http) {
	
	kvService.preuzmiV = function(){
		return $http.get("/kursUValuti/sviKurseviUValuti");
	}
	
	kvService.preuzmiV2 = function(id){
		return $http.get("/kursUValuti/sviKurseviUValutiValute/" + id);
	}
	
	kvService.sveValute = function(){
		return $http.get("/valuta/sveValute");
	}
	
	kvService.sveKursneListe = function(){
		return $http.get("/kursnaLista/sveKursneListe");
	}
	
	kvService.pretraziV = function(v,id1,id2,id3){
		return $http.put("/kursUValuti/pretraziKursUValuti/" + id1 + "/" + id2 + "/" + id3, v);
	}
	
	kvService.izmeniV = function(nm,id1,id2,id3){
		return $http.put("/kursUValuti/izmeniKursUValuti/" + id1 + "/" + id2 + "/" + id3 , nm);
	}
	
	kvService.regV = function(v,id1,id2,id3){
		return $http.post("/kursUValuti/dodajKursUValuti/" + id1 + "/" + id2 + "/" + id3, v);
	}
	
	kvService.izbrisiV = function(id){
		return $http.put("/kursUValuti/izbrisiKursUValuti/" + id);
	}
	return kvService;
});