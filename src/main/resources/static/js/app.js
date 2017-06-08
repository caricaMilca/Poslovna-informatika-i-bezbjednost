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
	})		
});
