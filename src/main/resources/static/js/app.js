var app = angular.module('webApp', [ 'ngRoute'  , 'ngNotify']);

// routeProvider
app.config(function($routeProvider, $locationProvider) {

	$routeProvider.when('login', {
		templateUrl : 'index.html',
		controller : 'appController'
	}).when('/Admin/registracijaSalteruse', {
		templateUrl: '/Admin/registracijaSalteruse.html',
		controller: 'appController'
	}).when('/Klijent/klijenti', {
		templateUrl: '/Klijent/klijenti.html',
		controller: 'klijentController'
	}).when('/changePassword', {
		templateUrl: '/univerzalno/changePassword.html',
		controller: 'appController'
	}).when('/nm/svaNM', {
		templateUrl: '/NaseljenoMesto/naseljenoMesto.html',
		controller: 'nmController'
	}).when('/Drzava/sveDrzave', {
		templateUrl: '/Drzava/drzava.html',
		controller: 'drzavaController'
	}).when('/valuta/valute', {
		templateUrl: '/valuta/valute.html',
		controller: 'valuteController'
	}).when('/Djelatnost/sveDjelatnosti', {
		templateUrl: '/Djelatnost/djelatnosti.html',
		controller: 'djelatnostController'
	}).when('/kursUValuti/kurs', {
		templateUrl: '/kursUValuti/kursUValuti.html',
		controller: 'kvController'		
	}).when('/VrstaPlacanja/vrstePlacanja', {
		templateUrl: '/VrstaPlacanja/vrstePlacanja.html',
		controller: 'vrstaPlacanjaController'
	}).when('/kursnaLista/lista', {
		templateUrl: '/kursnaLista/kursnaLista.html',
		controller: 'klController'
	})
});
