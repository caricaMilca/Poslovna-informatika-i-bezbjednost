var app = angular.module('webApp');

app
		.controller(
				'drzavaController',
				[
						'$rootScope',
						'$scope',
						'$location',
						'ngNotify',
						'drzavaService',
						function($rootScope, $scope, $location, ngNotify,
								drzavaService) {

							$scope.mode = 'nulto';
							if ($rootScope.kojeDrzave == 'valute') {
								drzavaService
										.preuzmiDrzaveValute($rootScope.nextFormValuta.id)
										.then(
												function(response) {
													if (response.data) {
														$scope.sveDrzave = response.data;
													}
												});
							} else {
								drzavaService
								.preuzmiDrzavu()
								.then(
										function(response) {
											if (response.data) {
												$scope.sveDrzave = response.data;
											}
										});
							}

							drzavaService.sveValute().then(function(response) {
								if (response.data) {
									$scope.valute = response.data;
									$scope.valutaDrzave = response.data[0];
								}
							});

							$scope.regDrzavu = function() {
								var id;
								if ($scope.valutaDrzave == -1)
									id = -1;
								else
									id = $scope.valutaDrzave.id;
								if ($scope.mode == 'add')
									drzavaService
											.regDrzavu($scope.novaDrzava,
													$scope.valutaDrzave.id)
											.then(
													function(response) {
														if (response.data) {
															ngNotify
																	.set(
																			'Uspjesna registracija',
																			{
																				type : 'success'
																			});
															$scope.sveDrzave
																	.push(response.data);
															$scope.novaDrzava = null;
															$scope.show = null;
														}
													});
								else if ($scope.mode == 'filter') {
									drzavaService
											.pretraziDrzave($scope.novaDrzava,
													id)
											.then(
													function(response) {
														if (response.data) {
															ngNotify
																	.set(
																			'Uspjesna pretraga',
																			{
																				type : 'success'
																			});
															$scope.sveDrzave = response.data;
															$scope.novaDrzava = null;
															$scope.show = null;
														}
													});
								} else if ($scope.mode == 'edit') {
									drzavaService
											.izmeniDrzavu($scope.novaDrzava,
													$scope.valutaDrzave.id)
											.then(
													function(response) {
														if (response.data) {
															ngNotify
																	.set(
																			'Uspjesna izmena',
																			{
																				type : 'success'
																			});

															var index = $scope.sveDrzave
																	.indexOf($scope.selectedDrzava);
															$scope.sveDrzave[index] = response.data;
															$scope.novaDrzava = response.data;
															$scope.drzava.id = response.data.id;
															$scope.mode = 'nulto';
														}
													});
								}

							}

							$scope.setSelectedValuta = function(selected) {
								$scope.valutaDrzave = selected;
							}

							$scope.setSelectedDrzava = function(selected) {
								$scope.selectedDrzava = selected;
								$scope.show = 10;
								$scope.novaDrzava = angular.copy(selected);
								$scope.mode = 'edit';
								$scope.valutaDrzave.zvanicnSifra = selected.valuta.zvanicnaSifra;
							}

							$scope.changeMode = function(tab) {
								$scope.novaDrzava = null;
								$scope.mode = tab;
								if (tab == 'filter')
									$scope.valutaDrzave = -1;
							}

							$scope.izbrisiDrzavu = function() {
								drzavaService
										.izbrisiDrzavu($scope.selectedDrzava.id)
										.then(
												function(response) {
													if (response.status == 200) {
														ngNotify
																.set(
																		'Uspjesno brisanje',
																		{
																			type : 'success'
																		});
														var index = $scope.sveDrzave
																.indexOf($scope.selectedDrzava);
														$scope.sveDrzave
																.splice(index,
																		1);
														$scope.novaDrzava = null;
														$scope.show = null;
														$scope.selectedDrzava = null;
													}

												});
							}

							$scope.first = function() {
								$scope.mode = 'edit';
								$scope.selectedDrzava = $scope.sveDrzave[0];
								$scope.drzava = $scope.selectedDrzava;
								$scope.novaDrzava = $scope.selectedDrzava;
							}

							$scope.last = function() {
								$scope.mode = 'edit';
								var i = $scope.sveDrzave.length - 1;
								$scope.selectedDrzava = $scope.sveDrzave[i];
								$scope.drzava = $scope.selectedDrzava;
								$scope.novaDrzava = $scope.selectedDrzava;
							}

							$scope.next = function() {
								$scope.mode = 'edit';
								var i = $scope.sveDrzave
										.indexOf($scope.selectedDrzava);
								if (i + 1 > $scope.sveDrzave.length - 1)
									$scope.selectedDrzava = $scope.sveDrzave[0];
								else
									$scope.selectedDrzava = $scope.sveDrzave[i + 1];
								$scope.drzava = $scope.selectedDrzava;
								$scope.novaDrzava = $scope.selectedDrzava;
							}

							$scope.prev = function() {
								$scope.mode = 'edit';
								var i = $scope.sveDrzave
										.indexOf($scope.selectedDrzava);
								if (i == 0)
									$scope.selectedDrzava = $scope.sveDrzave[$scope.sveDrzave.length - 1];
								else
									$scope.selectedDrzava = $scope.sveDrzave[i - 1];
								$scope.drzava = $scope.selectedDrzava;
								$scope.novaDrzava = $scope.selectedDrzava;
							}

							$scope.refreshTable = function() {
								drzavaService
										.preuzmiDrzavu()
										.then(
												function(response) {
													if (response.data) {
														$scope.sveDrzave = response.data;
														$scope.novaDrzava = null;
														$scope.mode = 'nulto';
														$scope.selectedDrzava = null;
														$rootScope.kojeDrzave = 'sve';
													}
												});
							}
							
							function refreash() {
								drzavaService
								.preuzmiDrzavu()
								.then(
										function(response) {
											if (response.data) {
												$scope.sveDrzave = response.data;
												$scope.novaDrzava = null;
												$scope.mode = 'nulto';
												$scope.selectedDrzava = null;
												$rootScope.kojeDrzave = 'sve';
											}
										});
							}

							$scope.odustani = function() {
								$scope.mode = 'nulto';
								$scope.selectedDrzava = null;
								$scope.novaDrzava = null;
								$scope.show = null;
								$rootScope.kojeDrzave = 'sve';
								refreash();
							}
							
							$scope.nextForm = function() {
								$rootScope.kojaNM = 'drzave';
								$rootScope.nextFormDrzava = $scope.selectedDrzava;
								$location.path('/nm/svaNM')
							}
						} ]);