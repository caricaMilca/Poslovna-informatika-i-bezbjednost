var app = angular.module('webApp');

app
		.controller(
				'vrstaPlacanjaController',
				[
						'$rootScope',
						'$scope',
						'$location',
						'ngNotify',
						'VrstaPlacanjaService',
						function($rootScope, $scope, $location, ngNotify,
								vrstaPlacanjaService) {

							$rootScope.kojiKursevi = '';
							$rootScope.kojiKlijenti = 'svi';
							$rootScope.kojaNM = ''
							$scope.mode = 'nulto';
							vrstaPlacanjaService
									.sveVrstePlacanja()
									.then(
											function(response) {
												if (response.data) {
													$scope.sveVrstePlacanja = response.data;
												}
											});

							$scope.regVrstaPlacanja = function() {
								if ($scope.mode == 'add')
									vrstaPlacanjaService
											.regVrstaPlacanja(
													$scope.vrstaPlacanja)
											.then(
													function(response) {
														if (response.data) {
															ngNotify
																	.set(
																			'Uspjesna registracija',
																			{
																				type : 'success'
																			});
															$scope.sveVrstePlacanja
																	.push(response.data);
															$scope.vrstaPlacanja = null;
															$scope.mode = 'nulto';
															$scope.selectedVrstaPlacanja = null;
														}
													});
								else if ($scope.mode == 'edit') {
									vrstaPlacanjaService
											.izmjeniVrstuPlacanja(
													$scope.vrstaPlacanja)
											.then(
													function(response) {
														if (response.data) {
															ngNotify
																	.set(
																			'Uspjesna izmjena',
																			{
																				type : 'success'
																			});
															var index = $scope.sveVrstePlacanja
																	.indexOf($scope.selectedVrstaPlacanja);
															$scope.sveVrstePlacanja[index] = response.data;
															$scope.vrstaPlacanja = response.data;
															$scope.selectedVrstaPlacanja = response.data;
															$scope.d = $scope.selectedVrstaPlacanja;
														}
													});
								}

							}

							$scope.izbrisiVrstuPlacanja = function() {
								vrstaPlacanjaService
										.izbrisiVrstuPlacanja(
												$scope.selectedVrstaPlacanja.id)
										.then(
												function(response) {
													if (response.status == 200) {
														ngNotify
																.set(
																		'Uspjesno brisanje',
																		{
																			type : 'success'
																		});
														var index = $scope.sveVrstePlacanja
																.indexOf($scope.selectedVrstaPlacanja);
														$scope.sveVrstePlacanja
																.splice(index,
																		1);
														$scope.vrstaPlacanja = null;
														$scope.mode = 'nulto';
														$scope.selectedVrstaPlacanja = null;

													}

												});
							}

							$scope.setSelectedVrstaPlacanja = function(selected) {
								$scope.selectedVrstaPlacanja = selected;
								$scope.vrstaPlacanja = angular.copy(selected);
								$scope.mode = 'edit';
							}

							$scope.changeMode = function(tab) {
								$scope.vrstaPlacanja = null;
								$scope.mode = tab;
							}

							$scope.first = function() {
								$scope.mode = 'edit';
								$scope.selectedVrstaPlacanja = $scope.sveVrstePlacanja[0];
								$scope.d = $scope.selectedVrstaPlacanja;
								$scope.vrstaPlacanja = $scope.selectedVrstaPlacanja;
							}

							$scope.last = function() {
								$scope.mode = 'edit';
								var i = $scope.sveVrstePlacanja.length - 1;
								$scope.selectedVrstaPlacanja = $scope.sveVrstePlacanja[i];
								$scope.d = $scope.selectedVrstaPlacanja;
								$scope.vrstaPlacanja = $scope.selectedVrstaPlacanja;
							}

							$scope.next = function() {
								$scope.mode = 'edit';
								var i = $scope.sveVrstePlacanja
										.indexOf($scope.selectedVrstaPlacanja);
								if (i + 1 > $scope.sveVrstePlacanja.length - 1)
									$scope.selectedVrstaPlacanja = $scope.sveVrstePlacanja[0];
								else
									$scope.selectedVrstaPlacanja = $scope.sveVrstePlacanja[i + 1];
								$scope.d = $scope.selectedVrstaPlacanja;
								$scope.vrstaPlacanja = $scope.selectedVrstaPlacanja;
							}

							$scope.prev = function() {
								$scope.mode = 'edit';
								var i = $scope.sveVrstePlacanja
										.indexOf($scope.selectedVrstaPlacanja);
								if (i == 0)
									$scope.selectedVrstaPlacanja = $scope.sveVrstePlacanja[$scope.sveVrstePlacanja.length - 1];
								else
									$scope.selectedVrstaPlacanja = $scope.sveVrstePlacanja[i - 1];
								$scope.d = $scope.selectedVrstaPlacanja;
								$scope.vrstaPlacanja = $scope.selectedVrstaPlacanja;
							}

							$scope.odustani = function() {
								$scope.mode = 'nulto';
								$scope.selectedVrstaPlacanja = null;
								$scope.vrstaPlacanja = null;
							}

						} ]);