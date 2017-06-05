var app = angular.module('webApp', [ 'ngRoute'  , 'ngNotify']);

// routeProvider
app.config(function($routeProvider, $locationProvider) {

	$routeProvider.when('login', {
		templateUrl : 'index.html',
		controller : 'appController'
	}).when('/univerzalno/registracijaKorisnika', {
		templateUrl: '/univerzalno/registracijaKorisnika.html',
		controller: 'appController'
	}).when('/Klijent/klijenti', {
		templateUrl: '/Klijent/klijenti.html',
		controller: 'appController'
	}).when('/changePassword', {
		templateUrl: '/univerzalno/changePassword.html',
		controller: 'appController'
	})		
});
