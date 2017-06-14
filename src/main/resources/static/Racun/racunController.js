var app = angular.module('webApp');

app
		.controller(
				'racunController',
				[
						'$rootScope',
						'$scope',
						'$location',
						'ngNotify',
						'RacunService',
						function($rootScope, $scope, $location, ngNotify,
								racunService) {

							$rootScope.kojiKursevi = '';
							$rootScope.kojiKlijenti = 'svi';
							$rootScope.kojaNM = ''
							$scope.todays = new Date();
							$scope.mode = 'Pregled';
							if ($rootScope.kojiRacuni == 'svi') {
								racunService
										.preuzmiRacune()
										.then(
												function(response) {
													if (response.data) {
														$scope.sviRacuni = response.data;
													}
												});
							} else if ($rootScope.kojiRacuni == 'klijenta') {
								racunService
										.sviRacuniKlijenta(
												$rootScope.nextFormKlijent.id)
										.then(
												function(response) {
													if (response.data) {
														$scope.sviRacuni = response.data;
														$scope.vlasnikRacuna = $rootScope.nextFormKlijent;
													}
												});
							} else if ($rootScope.kojiRacuni == 'valute') {
								racunService
										.sviRacuniValute(
												$rootScope.nextFormValuta.id)
										.then(
												function(response) {
													if (response.data) {
														$scope.sviRacuni = response.data;
														$scope.valutaRacuna = $rootScope.nextFormValuta;
													}
												});

							}
							$scope.vratiSve = function() {
								racunService
										.preuzmiRacune()
										.then(
												function(response) {
													if (response.data) {
														$scope.sviRacuni = response.data;
														$rootScope.kojiRacuni = 'svi';
														$scope.vlasnikRacuna = $scope.sviKlijenti[0];
														$scope.valutaRacuna = $scope.valute[0];
													}
												});
							}
							racunService.sviKlijenti().then(function(response) {
								if (response.data) {
									$scope.sviKlijenti = response.data;
									$scope.vlasnikRacuna = response.data[0];
								}
							});
							racunService.sveValute().then(function(response) {
								if (response.data) {
									$scope.valute = response.data;
									$scope.valutaRacuna = response.data[0];
								}
							});

							$scope.regRacuna = function() {
								var v_id;
								var k_id;
								if ($scope.valutaRacuna == -1)
									v_id = -1;
								else
									v_id = $scope.valutaRacuna.id;
								if ($scope.vlasnikRacuna == -1)
									k_id = -1;
								else
									k_id = $scope.vlasnikRacuna.id;
								if ($rootScope.kojiRacuni != 'svi') {
									if ($rootScope.kojiRacuni == 'klijenta')
										k_id = $rootScope.nextFormKlijent.id;
									if ($rootScope.kojiRacuni == 'valute')
										v_id = $rootScope.nextFormValuta.id;
									ngNotify
											.set(
													'U modu ste dodavanja u djelatnost odredjenu.',
													{
														type : 'info'
													});
								}
								if ($scope.mode == 'Dodavanje') {
									racunService
											.regRacuna($scope.noviRacun, k_id,
													v_id)
											.then(
													function(response) {
														if (response.data) {
															ngNotify
																	.set(
																			'Uspjesna registracija',
																			{
																				type : 'success'
																			});
															response.data.datumOtvaranja = moment(
																	response.data.datumOtvaranja)
																	.format(
																			'YYYY-MM-DD');
															if (response.data.datumZatvaranja != null)
																response.data.datumZatvaranja = moment(
																		response.data.datumZatvaranja)
																		.format(
																				'YYYY-MM-DD');
															$scope.sviRacuni
																	.push(response.data);
															$scope.noviRacun = null;
															$scope.mode = 'Pregled';
															$scope.selectedRacun = null;
														}
													});
								} else if ($scope.mode == 'Pretraga') {
									racunService
											.pretraziRacune($scope.noviRacun,
													k_id, v_id)
											.then(
													function(response) {
														if (response.data) {
															ngNotify
																	.set(
																			'Uspjesna pretraga',
																			{
																				type : 'success'
																			});
															$scope.sviRacuni = response.data;
															$scope.noviRacun = null;
															$scope.mode = 'Pregled';
															$scope.selectedRacun = null;
														}
													});
								}

							}

							$scope.zatvoriRacun = function(racun) {
								$scope.selectedRacun.racunPrenosa = racun;
								$scope.selectedRacun.racunPrenosa = $scope.racunPrenosa;
								racunService
										.zatvoriRacun($scope.selectedRacun)
										.then(
												function(response) {
													if (response.status == 200) {
														ngNotify
																.set(
																		'Uspjesno zatvaranje racuna.',
																		{
																			type : 'success'
																		});
														response.data.datumZatvaranja = moment(
																response.data.datumZatvaranja)
																.format(
																		'YYYY-MM-DD');
														var index = $scope.sviRacuni
																.indexOf($scope.selectedRacun);
														$scope.sviRacuni[index] = response.data;
														$scope.noviRacun = null;
														$scope.mode = 'Pregled';
														$scope.selectedRacun = null;
													}

												});
							}

							$scope.setSelectedKlijent = function(selected) {
								$scope.vlasnikRacuna = selected;
							}

							$scope.setSelectedValuta = function(selected) {
								$scope.valutaRacuna = selected;
							}

							$scope.setSelectedRacun = function(selected) {
								$scope.selectedRacun = selected;
								$scope.noviRacun = angular.copy(selected);
								$scope.noviRacun.datumOtvaranja = new Date(
										selected.datumOtvaranja);
								$scope.mode = 'Izmena';
								$scope.vlasnikRacuna.korisnickoIme = selected.klijent.korisnickoIme;
								$scope.valutaRacuna.zvanicnaSifra = selected.valuta.zvanicnaSifra;
							}

							$scope.changeMode = function(tab) {
								$scope.noviRacun = null;
								$scope.mode = tab;
								if (tab == 'Pretraga') {
									$scope.vlasnikRacuna = -1;
									$scope.valutaRacuna = -1;
								}
								if (tab == 'Dodavanje') {
									$scope.selectedRacun = null;
									if ($rootScope.kojiRacuni == 'svi') {
										$scope.vlasnikRacuna = $scope.sviKlijenti[0];
										$scope.valutaRacuna.zvanicnaSifra = $scope.valute[0].zvanicnaSifra;
									} else if ($rootScope.kojiRacuni == 'klijenta') {
										$scope.vlasnikRacuna.korisnickoIme = $rootScope.nextFormKlijent.korisnickoIme;
										$scope.valutaRacuna.zvanicnaSifra = $scope.valute[0].zvanicnaSifra;
									} else {
										$scope.vlasnikRacuna = $scope.sviKlijenti[0];
										$scope.valutaRacuna.zvanicnaSifra = $rootScope.nextFormValuta.zvanicnaSifra;
									}
								}

							}

							$scope.first = function() {
								$scope.mode = 'Izmena';
								$scope.selectedRacun = $scope.sviRacuni[0];
								$scope.k = $scope.selectedRacun;
								$scope.noviRacun = $scope.selectedRacun;
							}

							$scope.last = function() {
								$scope.mode = 'Izmena';
								var i = $scope.sviRacuni.length - 1;
								$scope.selectedRacun = $scope.sviRacuni[i];
								$scope.k = $scope.selectedRacun;
								$scope.noviRacun = $scope.selectedRacun;
							}

							$scope.next = function() {
								$scope.mode = 'Izmena';
								var i = $scope.sviRacuni
										.indexOf($scope.selectedRacun);
								if (i + 1 > $scope.sviRacuni.length - 1)
									$scope.selectedRacun = $scope.sviRacuni[0];
								else
									$scope.selectedRacun = $scope.sviRacuni[i + 1];
								$scope.k = $scope.selectedRacun;
								$scope.noviRacun = $scope.selectedRacun;
							}

							$scope.prev = function() {
								$scope.mode = 'Izmena';
								var i = $scope.sviRacuni
										.indexOf($scope.selectedRacun);
								if (i == 0)
									$scope.selectedRacun = $scope.sviRacuni[$scope.sviRacuni.length - 1];
								else
									$scope.selectedRacun = $scope.sviRacuni[i - 1];
								$scope.k = $scope.selectedRacun;
								$scope.noviRacun = $scope.selectedRacun;
							}

							$scope.refreshTable = function() {
								if ($rootScope.kojiRacuni == 'svi') {
									racunService
											.preuzmiRacune()
											.then(
													function(response) {
														if (response.data) {
															$scope.sviRacuni = response.data;
															$scope.noviRacun = null;
															$scope.mode = 'Pregled';
															$scope.selectedRacun = null;
														}
													});
								} else if ($rootScope.kojiRacuni == 'klijenta') {
									racunService
											.sviRacuniRacun(
													$rootScope.nextFormKlijent.id)
											.then(
													function(response) {
														if (response.data) {
															$scope.sviRacuni = response.data;
															$scope.noviRacun = null;
															$scope.mode = 'Pregled';
															$scope.selectedRacun = null;
															$scope.vlasnikRacuna = $rootScope.nextFormKlijent;
														}
													});
								} else if ($rootScope.kojiRacuni == 'valute') {
									racunService
											.sviRacuniValute(
													$rootScope.nextFormValuta.id)
											.then(
													function(response) {
														if (response.data) {
															$scope.sviRacuni = response.data;
															$scope.noviRacun = null;
															$scope.mode = 'Pregled';
															$scope.selectedRacun = null;
															$scope.valutaRacuna = $rootScope.nextFormValuta;
														}
													});

								}
							}

							$scope.odustani = function() {
								$scope.mode = 'Pregled';
								$scope.selectedRacun = null;
								$scope.noviRacun = null;
							}			
							
							$scope.prikaziDnevnaStanja= function() {
								$('#izaberiNextFormu').modal('hide');
								$rootScope.kojaStanja = 'racun';
								$rootScope.nextFormRacun = $scope.selectedRacun;
								$location.path('/DnevnoStanjeRacuna/dnevnaStanjaRacuna')
							}
							
							$scope.prikaziAnalitike = function() {
								$('#izaberiNextFormu').modal('hide');
								$rootScope.kojeAnalitike = 'racun';
								$rootScope.nextFormRacun = $scope.selectedRacun;
								$location.path('/AnalitikaIzvoda/analitike')
							}

						} ]);