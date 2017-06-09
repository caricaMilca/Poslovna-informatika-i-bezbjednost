var app = angular.module('webApp');

app
		.controller(
				'racunController',
				[
						'$rootScope',
						'$scope',
						'$location',
						'ngNotify',
						'racunService',
						function($rootScope, $scope, $location, ngNotify,
								racunService) {

							$scope.mode = 'nulto';
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
														$scope.klijentRacuna = $rootScope.nextFormKlijent;
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
														$scope.klijentRacuna = $scope.sviKlijenti[0];
														$scope.valutaRacuna = $scope.valute[0];
													}
												});
							}
							racunService.sviKlijenti().then(function(response) {
								if (response.data) {
									$scope.sviKlijenti = response.data;
									$scope.klijentRacuna = response.data[0];
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
								if ($scope.klijentRacuna == -1)
									k_id = -1;
								else
									k_id = $scope.klijentRacuna.id;
								if ($rootScope.kojiRacuni != 'svi') {
									if ($rootScope.kojiRacuni != 'klijenta')
										k_id = $rootScope.nextFormKlijent.id;
									if ($rootScope.kojiRacuni != 'valute')
										v_id = $rootScope.nextFormValuta.id;
									ngNotify
											.set(
													'U modu ste dodavanja u djelatnost odredjenu.',
													{
														type : 'info'
													});
								}
								if ($scope.mode == 'add') {
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
															$scope.sviRacuni
																	.push(response.data);
															$scope.noviRacun = null;
															$scope.mode = 'nulto';
															$scope.selectedRacun = null;
														}
													});
								} else if ($scope.mode == 'filter') {
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
															$scope.mode = 'nulto';
															$scope.selectedRacun = null;
														}
													});
								}

							}

							$scope.izbrisiRacun = function() {
								racunService
										.izbrisiRacun($scope.selectedRacun.id)
										.then(
												function(response) {
													if (response.status == 200) {
														ngNotify
																.set(
																		'Uspjesno brisanje',
																		{
																			type : 'success'
																		});
														var index = $scope.sviRacuni
																.indexOf($scope.selectedRacun);
														$scope.sviRacuni
																.splice(index,
																		1);
														$scope.noviRacun = null;
														$scope.mode = 'nulto';
														$scope.selectedRacun = null;

													}

												});
							}

							$scope.setSelectedKlijent = function(selected) {
								$scope.klijentRacuna = selected;
							}

							$scope.setSelectedValuta = function(selected) {
								$scope.valutaRacuna = selected;
							}

							$scope.setSelectedRacun = function(selected) {
								$scope.selectedRacun = selected;
								$scope.noviRacun = angular.copy(selected);
								$scope.mode = 'edit';
								$scope.klijentRacuna.korisnickoIme = selected.klijent.korisnickoIme;
								$scope.valutaRacuna.zvanicnaSifra = selected.valuta.zvanicnaSifra;
							}

							$scope.changeMode = function(tab) {
								$scope.noviRacun = null;
								$scope.mode = tab;
								if (tab == 'filter') {
									$scope.klijentRacuna = -1;
									$scope.valutaRacuna = -1;
								}
								if (tab == 'add'
										&& $rootScope.kojiRacuni == 'svi') {
									$scope.klijentRacuna = $scope.sviRacuni[0];
									$scope.valutaRacuna = $scope.valute[0];
								}

							}

							$scope.first = function() {
								$scope.mode = 'edit';
								$scope.selectedRacun = $scope.sviRacuni[0];
								$scope.k = $scope.selectedRacun;
								$scope.noviRacun = $scope.selectedRacun;
							}

							$scope.last = function() {
								$scope.mode = 'edit';
								var i = $scope.sviRacuni.length - 1;
								$scope.selectedRacun = $scope.sviRacuni[i];
								$scope.k = $scope.selectedRacun;
								$scope.noviRacun = $scope.selectedRacun;
							}

							$scope.next = function() {
								$scope.mode = 'edit';
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
								$scope.mode = 'edit';
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
															$scope.mode = 'nulto';
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
															$scope.mode = 'nulto';
															$scope.selectedRacun = null;
															$scope.klijentRacuna = $rootScope.nextFormKlijent;
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
															$scope.mode = 'nulto';
															$scope.selectedRacun = null;
															$scope.valutaRacuna = $rootScope.nextFormValuta;
														}
													});

								}
							}

							$scope.odustani = function() {
								$scope.mode = 'nulto';
								$scope.selectedRacun = null;
								$scope.noviRacun = null;
							}

						} ]);