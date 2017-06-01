var app = angular.module('webApp');

app.run([ 'ngNotify', function(ngNotify) {

	ngNotify.config({
		position : 'bottom',
		duration : 1000,
		theme : 'pitchy',
		sticky : false,
	});
} ]);

app.config([ '$qProvider', function($qProvider) {
	$qProvider.errorOnUnhandledRejections(false);
} ]);

app
		.controller(
				'appController',
				[
						'$rootScope',
						'$scope',
						'$location','ngNotify', 
						'SessionService',
						function($rootScope, $scope, $location,ngNotify, sessionService) {

							$scope.changePassword = function() {
								if($scope.lozinka.stara == $rootScope.korisnik.lozinka){
									sessionService.changePassword($scope.lozinka.nova).then(function(response){
										$location.path('/index');
									});
								} else {
									lozinka.nova = '';
									lozinka.stara = '';
									$location.path('/changePassword')
								}
							}
							
							$scope.logout = function(){
								sessionService.logout().then(function(response){
									$rootScope.korisnik = '';
									$location.path('/login');
								});
							}
							
							$scope.regSalterusu = function(){
								sessionService.regSalterusu($scope.salterusa).then(function(response){
									$location.path('/Admin/novaSalterusa');
								});
							}
							
							$scope.regKlijentaP = function(){
								sessionService.regKlijentaP($scope.klijent).then(function(response){
									$location.path('/salterusa/registracijaKlijenta');
								});
							}
							
							$scope.regKlijentaF = function(){
								sessionService.regKlijentaF($scope.klijent).then(function(response){
									$location.path('/obicnaSalterusa/registracijaKlijenataObicna');
								});
							}
							
							$scope.submitLogin = function() {
								sessionService
										.logIn($scope.logovanje)
										.then(
												function(response) {
													if (response.status == 200)
														if (response.data.uloga === "Klijent")
															sessionService
																	.preuzmiKlijenta()
																	.then(
																			function(
																					response) {
																				if (response.status == 200) {
																					ngNotify.set('Uspjesno logovanje' , {
																						type : 'success'
																					});
																					$rootScope.korisnik = response.data;
																					$location
																							.path('/Klijent/klijent');
																				}
																			});
														else
															sessionService
																	.preuzmiZaposlenog()
																	.then(
																			function(
																					response) {
																				if (response.status == 200) {
																					ngNotify.set('Uspjesno logovanje' , {
																						type : 'success'
																					});
																					$rootScope.korisnik = response.data;
																					if (response.data.ulogaZ === "Salterusa")
																						$location
																								.path('/obicnaSalterusa/registracijaKlijenataObicna');
																					else if (response.data.ulogaZ === "Super_salterusa")
																						$location
																								.path('/salterusa/registracijaKlijenta');
																					else
																						$location
																								.path('/Admin/novaSalterusa');
																					
																				}
																			});
													/*
													 * if (response.data ===
													 * "Klijent")
													 * $location.path('/Klijent/klijent');
													 * else if (response.data
													 * === "Salterusa")
													 * $location.path('/menadzerRestorana/restorani');
													 * else if (response.data
													 * === "Administrator")
													 * $location.path('/gost/profil');
													 * else (response.data ===
													 * "Super_salterusa")
													 * $location.path('/ponudjac/profili');
													 */

												}).catch(function(response) {
													ngNotify.set('Korisnik ne postoji' , {
														type : 'error',
													    sticky: true
													});
													console.error('Gists error', response.status, response.data)
												  });
							}

						} ]);
