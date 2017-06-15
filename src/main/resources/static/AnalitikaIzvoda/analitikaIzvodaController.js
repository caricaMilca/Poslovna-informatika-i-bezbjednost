var app = angular.module('webApp');

app
		.controller(
				'analitikaIzvodaController',
				[
						'$rootScope',
						'$scope',
						'$location',
						'ngNotify',
						'analitikaIzvodaService',
						function($rootScope, $scope, $location, ngNotify,
								analitikaIzvodaService) {
				
							$scope.mode = 'Pregled';
							$scope.nalog = null;

							
							if ($rootScope.kojeAnalitike == 'valute') {
								analitikaIzvodaService
										.sveAnalitikeValute(
												$rootScope.nextFormValuta.id)
										.then(
												function(response) {
													if (response.data) {
														document
																.getElementById("valuta").selected = $scope.nmDrzave;
														$scope.sviIzvodi = response.data;
														$scope.nmDrzave = $rootScope.nextFormValuta;
													}
												});
							}else if($rootScope.kojeAnalitike == 'racun'){
								analitikaIzvodaService
								.sveAnalitikeRacuna(
										$rootScope.nextFormRacun.id)
								.then(
										function(response) {
												$scope.sviIzvodi = response.data;
										});
							} else {
								analitikaIzvodaService.sveAnalitike().then(function(response) {
									if (response.data) {
										$scope.sviIzvodi = response.data;									
									}
								});
							}

							analitikaIzvodaService.sveValute().then(function(response) {
								if (response.data) {
									$scope.valute = response.data;
									$scope.valutaIzvoda = response.data[0];
								}
							});
							
							analitikaIzvodaService.sveVrstePlacanja().then(function(response) {
								if (response.data) {
									$scope.vrstePlacanja = response.data;
									$scope.vrstaPlacanjaIzvoda = response.data[0];
								}
							});

							$scope.dodajIzvod = function() {
								var id;
								if ($scope.valutaIzvoda == -1)
									id = -1;
								else
									id = $scope.valutaIzvoda.id;
								
								var id2;
								if ($scope.vrstaPlacanjaIzvoda == -1)
									id2 = -1;
								else
									id2 = $scope.vrstaPlacanjaIzvoda.id;
								
								if ($scope.mode == 'Pretraga') {
									analitikaIzvodaService
											.pretraziAnalitike($scope.noviIzvod)
											.then(
													function(response) {
														if (response.data) {
															ngNotify
																	.set(
																			'Uspjesna pretraga',
																			{
																				type : 'success'
																			});
															$scope.sviIzvodi= response.data;
															$scope.noviIzvod = null;
															$scope.show = null;
														}
													});
								}

							}

							$scope.setSelectedValuta = function(selected) {
								$scope.valutaIzvoda = selected;
							}
							
							$scope.setSelectedVrsta = function(selected) {
								$scope.vrstaPlacanjaIzvoda = selected;
							}

							$scope.setSelectedIzvod = function(selected) {
								$scope.mode == 'Pretraga'
								$scope.selectedIzvod = selected;
								$scope.show = 10;
								$scope.noviIzvod = angular.copy(selected);
								$scope.valutaIzvoda.naziv = selected.valuta.naziv;
								$scope.vrstaPlacanjaIzvoda.naziv = selected.vrstaPlacanja.naziv;
								document.getElementById("datumPrimanja").value = selected.datumPrimanja;
								document.getElementById("datumValute").value = selected.datumValute;
							}

							$scope.changeMode = function(tab) {
								$scope.noviIzvod = null;
								$scope.mode = tab;
								if (tab == 'Pregled'){
									$scope.valutaIzvoda = -1;
									$scope.vrstaPlacanjaIzvoda = -1;
								}
							}

						
							$scope.first = function() {
								$scope.mode == 'Pretraga'
								$scope.selectedIzvod = $scope.izvodi[0];
								$scope.izvod = $scope.selectedIzvod;
								$scope.noviIzvod = $scope.selectedIzvod;
							}

							$scope.last = function() {
								$scope.mode == 'Pretraga'
								var i = $scope.izvodi.length - 1;
								$scope.selectedIzvod = $scope.izvodi[i];
								$scope.izvod = $scope.selectedIzvod;
								$scope.noviIzvod = $scope.selectedIzvod;
							}

							$scope.next = function() {
								$scope.mode == 'Pretraga'
								var i = $scope.izvodi.indexOf($scope.selectedIzvod);
								if (i + 1 > $scope.izvodi.length - 1)
									$scope.selectedIzvod = $scope.izvodi[0];
								else
									$scope.selectedIzvod = $scope.izvodi[i + 1];
								$scope.izvod = $scope.selectedIzvod;
								$scope.noviIzvod = $scope.selectedIzvod;
							}

							$scope.prev = function() {
								$scope.mode == 'Pretraga'
								var i = $scope.izvodi.indexOf($scope.selectedIzvod);
								if (i == 0)
									$scope.selectedIzvod = $scope.izvodi[$scope.izvodi.length - 1];
								else
									$scope.selectedIzvod = $scope.izvodi[i - 1];
								$scope.izvod = $scope.selectedIzvod;
								$scope.noviIzvod = $scope.selectedIzvod;
							}

							$scope.refreshTable = function() {
								$rootScope.kojeAnalitike = 'sve';
								analitikaIzvodaService.sveAnalitike().then(function(response) {
									if (response.data) {
										$scope.sviIzvodi = response.data;
										$scope.noviIzvod = null;
										$scope.mode = 'Pregled';
										$scope.selectedIzvod = null;
										// $scope.noviIzvod.datumPrimanja =
										// null;
										// $scope.noviIzvod.datumValute = null;
										document.getElementById("datumPrimanja").value = "";
										document.getElementById("datumValute").value = "";
									}
								});
							}
							
							function refresh() {
								analitikaIzvodaService.sveAnalitike().then(function(response) {
									if (response.data) {
									$scope.izvodi = response.data;
										$scope.noviIzvod = null;
										$scope.mode = 'Pregled';
										$scope.selectedIzvod = null;
										document.getElementById("datumPrimanja").value = "";
										document.getElementById("datumValute").value = "";
									}
								});
							}

							$scope.odustani = function() {
								$scope.mode = 'Pregled';
								$scope.selectedIzvod = null;
								$scope.noviIzvod = null;
								$scope.show = null;
								$rootScope.kojeAnalitike = 'sve';
								refresh();
							}
							
							
							
							$scope.kreiraj = function(){
								$scope.nalog.tipTransakcije = $scope.tipTransakcije;
								analitikaIzvodaService.kreiraj($scope.nalog).then(function(response) {
									if (response.status == 201) {
										ngNotify.set('Prosla transakcija', {
											type : 'success'
										});
										$scope.nalog = null;
									} else if(response.status == 202){
										ngNotify.set('Ne postoje sredstva na racunu duznika za izvrsenje transakcije.', {
											type : 'info'
										});
									}
								}).catch(function(response) {
									ngNotify.set('Valuta ne postoji, ili brojRacuna nije validan ' , {
										type : 'error',
									    sticky: true
									});
									console.error('Gists error', response.status, response.data)
								  });
							}
							
							$scope.ucitajFajl = function() {
								 var file = document.getElementById("imeFajla").files[0]
								 if(file != null || file != undefined) {	
									 analitikaIzvodaService.ucitajTransakcije(file.name).then(function(response) {
											if (response.status == 200) {
												ngNotify
												.set(
														'Uspjesno ucitavanje transakcija',
														{
															type : 'success'
														});
												analitikaIzvodaService.sveAnalitike().then(function(response) {
													if (response.data) {
														$scope.sviIzvodi = response.data;									
													}
												});
											} else {
												ngNotify
												.set(
														'Doslo je do greske prilikom ucitavanja fajla',
														{
															type : 'error'
														});
											}
										});
								 }
								 $('#izaberiFajl').modal('hide');
							}
							
							$scope.exportMedjubankarskiPrenos = function() {
								 analitikaIzvodaService.exportMedjubankarskogPrenosa(1).then(function(response) {
										if (response.status == 200) {
											ngNotify
											.set(
													'Uspjesno exportovan medjubankarski prenos',
													{
														type : 'success'
													});
										}
								 });
							}
						} ]);