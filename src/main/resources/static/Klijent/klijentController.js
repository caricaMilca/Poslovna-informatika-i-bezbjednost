var app = angular.module('webApp');

app
		.controller(
				'klijentController',
				[
						'$rootScope',
						'$scope',
						'$location',
						'ngNotify',
						'KlijentService',
						function($rootScope, $scope, $location, ngNotify,
								klijentService) {

							$rootScope.kojiKursevi = '';
							$rootScope.kojaNM = ''
							$scope.mode = 'Pregled';
							if($rootScope.kojiKlijenti == 'svi'){
								klijentService.preuzmiKlijente().then(
										function(response) {
											if (response.data) {
												$scope.sviKlijenti = response.data;
											}
										});
							} else if($rootScope.kojiKlijenti == 'djelatnosti'){
								klijentService.sviKlijentiDjelatnosti($rootScope.nextFormDjelatnost.id).then(
										function(response) {
											if (response.data) {
												$scope.sviKlijenti = response.data;
												$scope.djelatnostKlijenta = $rootScope.nextFormDjelatnost;
											}
										});
								
							}
							$scope.vratiSve = function() {
								klijentService.preuzmiKlijente().then(
										function(response) {
											if (response.data) {
												$scope.sviKlijenti = response.data;
												$rootScope.kojiKlijenti = 'svi';
												$scope.djelatnostKlijenta = $scope.djelatnosti[0];
											}
										});
							}
							klijentService
									.sveDjelatnosti()
									.then(
											function(response) {
												if (response.data) {
													$scope.djelatnosti = response.data;
													$scope.djelatnostKlijenta = response.data[0];
												}
											});

							$scope.regKlijenta = function() {
								var id;
								if ($scope.djelatnostKlijenta == -1)
									id = -1;
								else
									id = $scope.djelatnostKlijenta.id;
								if($rootScope.kojiKlijenti != 'svi'){
									$scope.noviKlijent.ulogaK = 'POSLOVNO';
									id = $rootScope.nextFormDjelatnost.id;
									ngNotify
									.set(
											'U modu ste dodavanja u djelatnost odredjenu.',
											{
												type : 'info'
											});
								}
								if ($scope.mode == 'Dodavanje') {
									klijentService
											.regKlijenta(
													$scope.noviKlijent,
													id)
											.then(
													function(response) {
														if (response.data) {
															ngNotify
																	.set(
																			'Uspjesna registracija',
																			{
																				type : 'success'
																			});
															$scope.sviKlijenti
																	.push(response.data);
															$scope.noviKlijent = null;
															$scope.mode = 'Pregled';
															$scope.selectedKlijent = null;
														}
													});
								} else if ($scope.mode == 'Pretraga') {
									klijentService
											.pretraziKlijente(
													$scope.noviKlijent, id)
											.then(
													function(response) {
														if (response.data) {
															ngNotify
																	.set(
																			'Uspjesna pretraga',
																			{
																				type : 'success'
																			});
															$scope.sviKlijenti = response.data;
															$scope.noviKlijent = null;
															$scope.mode = 'Pregled';
															$scope.selectedKlijent = null;
														}
													});
								} else if ($scope.mode == 'Izmena') {
									klijentService
											.izmjeniKlijenta(
													$scope.noviKlijent,
													id)
											.then(
													function(response) {
														if (response.data) {
															ngNotify
																	.set(
																			'Uspjesna izmjena',
																			{
																				type : 'success'
																			});
															var index = $scope.sviKlijenti
																	.indexOf($scope.selectedKlijent);
															$scope.sviKlijenti[index] = response.data;
															$scope.noviKlijent = response.data;
															$scope.selectedKlijent = response.data;
															$scope.k = $scope.selectedKlijent;
														}
													});
								}

							}

							$scope.izbrisiKlijenta = function() {
								klijentService
										.izbrisiKlijenta(
												$scope.selectedKlijent.id)
										.then(
												function(response) {
													if (response.status == 200) {
														ngNotify
																.set(
																		'Uspjesno brisanje',
																		{
																			type : 'success'
																		});
														var index = $scope.sviKlijenti
																.indexOf($scope.selectedKlijent);
														$scope.sviKlijenti
																.splice(index,
																		1);
														$scope.noviKlijent = null;
														$scope.mode = 'Pregled';
														$scope.selectedKlijent = null;

													}

												});
							}

							$scope.setSelectedDjelatnost = function(selected) {
								$scope.djelatnostKlijenta = selected;
							}

							$scope.setSelectedKlijent = function(selected) {
								$scope.selectedKlijent = selected;
								$scope.show = 10;
								$scope.noviKlijent = angular.copy(selected);
								$scope.mode = 'Izmena';
								if (selected.ulogaK == 'POSLOVNO'){
									$scope.djelatnostKlijenta.sifra = selected.djelatnost.sifra;
								} else
									$scope.djelatnostKlijenta = -1;
							}

							$scope.changeMode = function(tab) {
								$scope.djelatnostKlijenta = -1;
								$scope.noviKlijent = null;
								$scope.mode = tab;
								if (tab == 'Pretraga')
									$scope.djelatnostKlijenta = -1;
								if(tab == 'Dodavanje') { 
									$scope.selectedKlijent = null;
									if($rootScope.kojiKlijenti == 'svi')
									$scope.djelatnostKlijenta = $scope.djelatnosti[0];
								}
								
							}

							$scope.first = function() {
								$scope.mode = 'Izmena';
								$scope.selectedKlijent = $scope.sviKlijenti[0];
								$scope.k = $scope.selectedKlijent;
								$scope.noviKlijent = $scope.selectedKlijent;
							}

							$scope.last = function() {
								$scope.mode = 'Izmena';
								var i = $scope.sviKlijenti.length - 1;
								$scope.selectedKlijent = $scope.sviKlijenti[i];
								$scope.k = $scope.selectedKlijent;
								$scope.noviKlijent = $scope.selectedKlijent;
							}

							$scope.next = function() {
								$scope.mode = 'Izmena';
								var i = $scope.sviKlijenti
										.indexOf($scope.selectedKlijent);
								if (i + 1 > $scope.sviKlijenti.length - 1)
									$scope.selectedKlijent = $scope.sviKlijenti[0];
								else
									$scope.selectedKlijent = $scope.sviKlijenti[i + 1];
								$scope.k = $scope.selectedKlijent;
								$scope.noviKlijent = $scope.selectedKlijent;
							}

							$scope.prev = function() {
								$scope.mode = 'Izmena';
								var i = $scope.sviKlijenti
										.indexOf($scope.selectedKlijent);
								if (i == 0)
									$scope.selectedKlijent = $scope.sviKlijenti[$scope.sviKlijenti.length - 1];
								else
									$scope.selectedKlijent = $scope.sviKlijenti[i - 1];
								$scope.k = $scope.selectedKlijent;
								$scope.noviKlijent = $scope.selectedKlijent;
							}

							$scope.refreshTable = function() {
								if($rootScope.kojiKlijenti == 'svi'){
									klijentService.preuzmiKlijente().then(
											function(response) {
												if (response.data) {
													$scope.sviKlijenti = response.data;
													$scope.noviKlijent = null;
													$scope.mode = 'Pregled';
													$scope.selectedKlijent = null;
												}
											});
								} else if($rootScope.kojiKlijenti == 'djelatnosti'){
									klijentService.sviKlijentiDjelatnosti($rootScope.nextFormDjelatnost.id).then(
											function(response) {
												if (response.data) {
													$scope.sviKlijenti = response.data;
													$scope.noviKlijent = null;
													$scope.mode = 'Pregled';
													$scope.selectedKlijent = null;
													$scope.djelatnostKlijenta = $rootScope.nextFormDjelatnost;
												}
											});
									
								}
							}

							$scope.odustani = function() {
								$scope.mode = 'Pregled';
								$scope.selectedKlijent = null;
								$scope.noviKlijent = null;
							}
							
							$scope.nextForm = function() {
								$rootScope.kojiRacuni = 'klijenta';
								$rootScope.nextFormKlijent = $scope.selectedKlijent;
								$location.path('/Racun/racuni')
							}

						} ]);