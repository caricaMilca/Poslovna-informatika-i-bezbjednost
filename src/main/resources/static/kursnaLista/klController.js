var app = angular.module('webApp');

app
		.controller(
				'klController',
				[
						'$rootScope',
						'$scope',
						'$location',
						'ngNotify',
						'KLService',
						function($rootScope, $scope, $location, ngNotify,
								klService) {

							$rootScope.kojiKlijenti = 'svi';
							$rootScope.kojaNM = ''
							$scope.mode = 'Pregled';

							klService.preuzmiNM().then(function(response) {
								if (response.data) {
									$scope.svaNM = response.data;
								}
							});

							$scope.regNM = function() {
								if ($scope.mode == 'Dodavanje') {
									klService
											.regNM($scope.novoNM)
											.then(
													function(response) {
														if (response.data) {
															ngNotify
																	.set(
																			'Uspesno dodavanje',
																			{
																				type : 'success'
																			});
															$scope.svaNM
																	.push(response.data);
															$scope.novoNM = null;
															$scope.show = null;
														}
														refresh();
													});
								} else if ($scope.mode == 'Pretraga') {
									klService
											.pretraziNM($scope.novoNM)
											.then(
													function(response) {
														if (response.data) {
															ngNotify
																	.set(
																			'Uspesna pretraga',
																			{
																				type : 'success'
																			});
															$scope.svaNM = response.data;
															$scope.novoNM = null;
															$scope.show = null;
														}
													});
								} else if ($scope.mode == 'Izmena') {
									klService
											.izmeniNM($scope.novoNM)
											.then(
													function(response) {
														if (response.data) {
															ngNotify
																	.set(
																			'Uspesna izmena',
																			{
																				type : 'success'
																			});

															var index = $scope.svaNM
																	.indexOf($scope.selectedNM);
															$scope.svaNM[index] = response.data;
															$scope.novoNM = response.data;
															$scope.nm.id = response.data.id;
															$scope.mode = 'Pregled';															
														}
														refresh();
													});
								}

							}

							$scope.setSelectedNM = function(selected) {
								$scope.selectedNM = selected;
								$scope.show = 10;
								$scope.novoNM = angular.copy(selected);
								$scope.mode = 'Izmena';
								document.getElementById("datum").value = selected.datum;
								document.getElementById("primjenjujeSeOd").value = selected.primjenjujeSeOd;
							}

							$scope.changeMode = function(tab) {
								$scope.novoNM = null;
								$scope.mode = tab;
							}

							$scope.izbrisiNM = function() {
								klService
										.izbrisiNM($scope.selectedNM.id)
										.then(
												function(response) {
													if (response.status == 200) {
														ngNotify
																.set(
																		'Uspjesno brisanje',
																		{
																			type : 'success'
																		});
														var index = $scope.svaNM
																.indexOf($scope.selectedNM);
														$scope.svaNM.splice(
																index, 1);
														$scope.novoNM = null;
														$scope.show = null;
														$scope.selectedNM = null;
													}

												});
							}

							$scope.first = function() {
								$scope.mode = 'Izmena';
								$scope.selectedNM = $scope.svaNM[0];
								$scope.nm = $scope.selectedNM;
								$scope.novoNM = $scope.selectedNM;
							}

							$scope.last = function() {
								$scope.mode = 'Izmena';
								var i = $scope.svaNM.length - 1;
								$scope.selectedNM = $scope.svaNM[i];
								$scope.nm = $scope.selectedNM;
								$scope.novoNM = $scope.selectedNM;
							}

							$scope.next = function() {
								$scope.mode = 'Izmena';
								var i = $scope.svaNM.indexOf($scope.selectedNM);
								if (i + 1 > $scope.svaNM.length - 1)
									$scope.selectedNM = $scope.svaNM[0];
								else
									$scope.selectedNM = $scope.svaNM[i + 1];
								$scope.nm = $scope.selectedNM;
								$scope.novoNM = $scope.selectedNM;
							}

							$scope.prev = function() {
								$scope.mode = 'Izmena';
								var i = $scope.svaNM.indexOf($scope.selectedNM);
								if (i == 0)
									$scope.selectedNM = $scope.svaNM[$scope.svaNM.length - 1];
								else
									$scope.selectedNM = $scope.svaNM[i - 1];
								$scope.nm = $scope.selectedNM;
								$scope.novoNM = $scope.selectedNM;
							}

							$scope.refreshTable = function() {
								klService.preuzmiNM().then(function(response) {
									if (response.data) {
										$scope.svaNM = response.data;
										$scope.novoNM = null;
										$scope.mode = 'Pregled';
										$scope.selectedNM = null;
									}
								});
							}

							function refresh() {
								klService.preuzmiNM().then(function(response) {
									if (response.data) {
										$scope.svaNM = response.data;
										$scope.novoNM = null;
										$scope.mode = 'Pregled';
										$scope.selectedNM = null;
									}
								});
								s
							}

							$scope.odustani = function() {
								$scope.mode = 'Pregled';
								$scope.selectedNM = null;
								$scope.novoNM = null;
								$scope.show = null;
							}

						} ]);