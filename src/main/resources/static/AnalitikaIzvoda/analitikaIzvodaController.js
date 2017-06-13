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
											.pretraziAnalitike($scope.noviIzvod, id,id2)
											.then(
													function(response) {
														if (response.data) {
															ngNotify
																	.set(
																			'Uspjesna pretraga',
																			{
																				type : 'success'
																			});
															$scope.valute = response.data;
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
								$scope.selectedIzvod = selected;
								$scope.show = 10;
								$scope.noviIzvod = angular.copy(selected);
								$scope.mode = 'Izmena';
								$scope.valutaIzvoda.naziv = selected.valuta.naziv;
								$scope.vrstaPlacanjaIzvoda.naziv = selected.vrstaPlacanja.naziv;
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
								$scope.mode = 'Izmena';
								$scope.selectedIzvod = $scope.izvodi[0];
								$scope.izvod = $scope.selectedIzvod;
								$scope.noviIzvod = $scope.selectedIzvod;
							}

							$scope.last = function() {
								$scope.mode = 'Izmena';
								var i = $scope.izvodi.length - 1;
								$scope.selectedIzvod = $scope.izvodi[i];
								$scope.izvod = $scope.selectedIzvod;
								$scope.noviIzvod = $scope.selectedIzvod;
							}

							$scope.next = function() {
								$scope.mode = 'Izmena';
								var i = $scope.izvodi.indexOf($scope.selectedIzvod);
								if (i + 1 > $scope.izvodi.length - 1)
									$scope.selectedIzvod = $scope.izvodi[0];
								else
									$scope.selectedIzvod = $scope.izvodi[i + 1];
								$scope.izvod = $scope.selectedIzvod;
								$scope.noviIzvod = $scope.selectedIzvod;
							}

							$scope.prev = function() {
								$scope.mode = 'Izmena';
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
										$scope.izvodi = response.data;
										$scope.noviIzvod = null;
										$scope.mode = 'Pregled';
										$scope.selectedIzvod = null;
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
										ngNotify.set('Uspjesno izvrsena transakcija', {
											type : 'success'
										});
										$scope.nalog = null;
									} else {
										ngNotify.set('Provjerite unos', {
											type : 'error'
										});
									}
								});
							}
						} ]);