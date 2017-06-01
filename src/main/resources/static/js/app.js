var app = angular.module('webApp', [ 'ngRoute'  , 'ngNotify']);

// routeProvider
app.config(function($routeProvider, $locationProvider) {

	$routeProvider.when('login', {
		templateUrl : 'index.html',
		controller : 'appController'
	}).when('/Admin/novaSalterusa', {
		templateUrl: '/Admin/novaSalterusa.html',
		controller: 'appController'
	}).when('/Klijent/klijent', {
		templateUrl: '/Klijent/klijent.html',
		controller: 'appController'
	}).when('/salterusa/registracijaKlijenta', {
		templateUrl: '/salterusa/registracijaKlijenta.html',
		controller: 'appController'
	}).when('/obicnaSalterusa/registracijaKlijenataObicna', {
		templateUrl: '/obicnaSalterusa/registracijaKlijenataObicna.html',
		controller: 'appController'
	}).when('/changePassword', {
		templateUrl: '/pages/changePassword.html',
		controller: 'appController'
	})		
});
