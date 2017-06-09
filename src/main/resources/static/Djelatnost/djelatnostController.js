var app = angular.module('webApp');

app
		.controller(
				'djelatnostController',
				[
						'$rootScope',
						'$scope',
						'$location',
						'ngNotify',
						'DjelatnostService',
						function($rootScope, $scope, $location, ngNotify,
								djelatnostService) {

							$scope.mode = 'nulto';
							djelatnostService.sveDjelatnosti().then(
									function(response) {
										if (response.data) {
											$scope.sveDjelatnosti = response.data;
										}
									});

							$scope.regDjelatnost = function() {
								if ($scope.mode == 'add')
									djelatnostService
											.regDjelatnost(
													$scope.djelatnost)
											.then(
													function(response) {
														if (response.data) {
															ngNotify
																	.set(
																			'Uspjesna registracija',
																			{
																				type : 'success'
																			});
															$scope.sveDjelatnosti
																	.push(response.data);
															$scope.djelatnost = null;
															$scope.mode = 'nulto';
															$scope.selectedDjelatnost = null;
														}
													});
								else if ($scope.mode == 'filter') {
									djelatnostService
											.pretraziDjelatnosti(
													$scope.djelatnost)
											.then(
													function(response) {
														if (response.data) {
															ngNotify
																	.set(
																			'Uspjesna pretraga',
																			{
																				type : 'success'
																			});
															$scope.sveDjelatnosti = response.data;
															$scope.djelatnost = null;
															$scope.mode = 'nulto';
															$scope.selectedDjelatnost = null;
														}
													});
								} else if ($scope.mode == 'edit') {
									djelatnostService
											.izmjeniDjelatnost(
													$scope.djelatnost)
											.then(
													function(response) {
														if (response.data) {
															ngNotify
																	.set(
																			'Uspjesna izmjena',
																			{
																				type : 'success'
																			});
															var index = $scope.sveDjelatnosti
																	.indexOf($scope.selectedDjelatnost);
															$scope.sveDjelatnosti[index] = response.data;
															$scope.djelatnost = response.data;
															$scope.selectedDjelatnost = response.data;
															$scope.d = $scope.selectedDjelatnost;
														}
													});
								}

							}

							$scope.izbrisiDjelatnost = function() {
								djelatnostService
										.izbrisiDjelatnost(
												$scope.selectedDjelatnost.id)
										.then(
												function(response) {
													if (response.status == 200) {
														ngNotify
																.set(
																		'Uspjesno brisanje',
																		{
																			type : 'success'
																		});
														var index = $scope.sveDjelatnosti
																.indexOf($scope.selectedDjelatnost);
														$scope.sveDjelatnosti
																.splice(index,
																		1);
														$scope.djelatnost = null;
														$scope.mode = 'nulto';
														$scope.selectedDjelatnost = null;

													}

												});
							}

							$scope.setSelectedDjelatnost = function(selected) {
								/*if($rootScope.korisnik.ulogaZ != 'Administrator'){
									ngNotify
									.set(
											'Nemate prava izmjene',
											{
												type : 'info'
											});
									return;
								}
									*/
								$scope.selectedDjelatnost = selected;
								$scope.show = 10;
								$scope.djelatnost = angular.copy(selected);
								$scope.mode = 'edit';
							}

							$scope.changeMode = function(tab) {
								$scope.djelatnost = null;
								$scope.mode = tab;
							}

							$scope.first = function() {
								$scope.mode = 'edit';
								$scope.selectedDjelatnost = $scope.sveDjelatnosti[0];
								$scope.d = $scope.selectedDjelatnost;
								$scope.djelatnost = $scope.selectedDjelatnost;
							}

							$scope.last = function() {
								$scope.mode = 'edit';
								var i = $scope.sveDjelatnosti.length - 1;
								$scope.selectedDjelatnost = $scope.sveDjelatnosti[i];
								$scope.d = $scope.selectedDjelatnost;
								$scope.djelatnost = $scope.selectedDjelatnost;
							}

							$scope.next = function() {
								$scope.mode = 'edit';
								var i = $scope.sveDjelatnosti
										.indexOf($scope.selectedDjelatnost);
								if (i + 1 > $scope.sveDjelatnosti.length - 1)
									$scope.selectedDjelatnost = $scope.sveDjelatnosti[0];
								else
									$scope.selectedDjelatnost = $scope.sveDjelatnosti[i + 1];
								$scope.d = $scope.selectedDjelatnost;
								$scope.djelatnost = $scope.selectedDjelatnost;
							}

							$scope.prev = function() {
								$scope.mode = 'edit';
								var i = $scope.sveDjelatnosti
										.indexOf($scope.selectedDjelatnost);
								if (i == 0)
									$scope.selectedDjelatnost = $scope.sveDjelatnosti[$scope.sveDjelatnosti.length - 1];
								else
									$scope.selectedDjelatnost = $scope.sveDjelatnosti[i - 1];
								$scope.d = $scope.selectedDjelatnost;
								$scope.djelatnost = $scope.selectedDjelatnost;
							}

							$scope.refreashTable = function() {
								djelatnostService
										.sveDjelatnosti()
										.then(
												function(response) {
													if (response.data) {
														$scope.sveDjelatnosti = response.data;
														$scope.djelatnost = null;
														$scope.mode = 'nulto';
														$scope.selectedDjelatnost = null;
													}
												});
							}
							
							$scope.odustani = function() {
								$scope.mode = 'nulto';
								$scope.selectedKlijent = null;
								$scope.noviKlijent = null;
							}


						} ]);