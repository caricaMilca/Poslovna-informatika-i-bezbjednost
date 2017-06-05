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
							sessionService
									.preuzmiKlijenta()
									.then(
											function(
													response) {
												if (response.status == 200) {
													$rootScope.korisnik = response.data;
												}
											});
							sessionService
							.preuzmiZaposlenog()
							.then(
									function(
											response) {
										if (response.status == 200) {
											$rootScope.korisnik = response.data;
											$location.path('/univerzalno/registracijaKorisnika');
											if(response.data.ulogaZ === 'Super_salterusa')
												sessionService
												.sveDjelatnosti()
												.then(
														function(response) {
															if (response.status == 200) {
																$scope.djelatnosti = response.data;
																  $scope.djelatnostKlijenta = $scope.djelatnosti[0]; s
															}
												
											});
										}
									});
							
							if($rootScope.korisnik == undefined)
								$location.path('/index');
							
								$scope.changePassword = function() {
								if($scope.lozinka.stara == $rootScope.korisnik.lozinka){
									sessionService.changePassword($scope.lozinka.nova).then(function(response){
										$location.path('/index');
										ngNotify.set('Uspjesno promjenjena lozinka' , {
											type : 'success'
										});
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
									$scope.logovanje = null;
								});
							}
							
							$scope.regSalterusu = function(){
								sessionService.regSalterusu($scope.novi).then(function(response){
									$location.path('/univerzalno/registracijaKorisnika');
									ngNotify.set('Uspjesna registracija' , {
										type : 'success'
									});
									$scope.novi = null;
								});
							}
							
							$scope.regKlijentaP = function(){
								sessionService.regKlijentaP($scope.novi, $scope.djelatnostKlijenta.id).then(function(response){
									$location.path('/univerzalno/registracijaKorisnika');
									ngNotify.set('Uspjesna registracija' , {
										type : 'success'
									});
									$scope.novi = null;
								});
							}
							
							$scope.regKlijentaF = function(){
								sessionService.regKlijentaF($scope.novi).then(function(response){
									$location.path('/univerzalno/registracijaKorisnika');
									ngNotify.set('Uspjesna registracija' , {
										type : 'success'
									});
									$scope.novi = null;
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
																								.path('/univerzalno/registracijaKorisnika');
																					else if (response.data.ulogaZ === "Super_salterusa") {
																						$location
																								.path('/univerzalno/registracijaKorisnika');
																						sessionService
																						.sveDjelatnosti()
																						.then(
																								function(response) {
																									if (response.status == 200) {
																										$scope.djelatnosti = response.data;
																										  $scope.djelatnostKlijenta = $scope.djelatnosti[0]; 
																									}
																						
																					});
																						}
																					else
																						$location
																								.path('/univerzalno/registracijaKorisnika');
																					
																				}
																			});

												}).catch(function(response) {
													ngNotify.set('Korisnik ne postoji' , {
														type : 'error',
													    sticky: true
													});
													console.error('Gists error', response.status, response.data)
												  });
							}
							
							$scope.setSelectedDjelatnost = function(
									selected) {
								$scope.djelatnostKlijenta = selected;
							}

							
						} ]);
