var app = angular.module('webApp');

app
		.controller(
				'dsrController',
				[
						'$rootScope',
						'$scope',
						'$location',
						'ngNotify',
						'dsrService',
						function($rootScope, $scope, $location, ngNotify,
								dsrService) {

							if ($rootScope.kojaStanja == 'racun') {
								dsrService
										.svaDSRacuna(
												$rootScope.nextFormRacun.id)
										.then(
												function(response) {
														$scope.dnevnaStanjaRacuna = response.data;
												});
							}
							
							dsrService.sviRacuni().then(function(response) {
								if (response.data) {
									$scope.racuni = response.data;
									$scope.racunDSR = response.data[0];
								}
							});

							$scope.regDSR = function() {
								if ($scope.mode == 'Pretraga') {
									dsrService
											.pretraziDSR($scope.novoDSR,
													$rootScope.nextFormRacun.id)
											.then(
													function(response) {
														if (response.data) {
															ngNotify
																	.set(
																			'Uspjesna pretraga',
																			{
																				type : 'success'
																			});
															$scope.racuni = response.data;
															$scope.novoDSR = null;
															$scope.show = null;
														}
													});
								}
							}
							
							
							$scope.setSelectedRacun = function(selected) {
								$scope.racunDSR = selected;
							}

							$scope.setselectedDSR = function(selected) {
								$scope.selectedDSR = selected;
								$scope.show = 10;
								$scope.novoDSR = angular.copy(selected);
								$scope.mode = 'Izmena';
								$scope.drzavaNM.sifra = selected.drzava.sifra;
							}

							$scope.changeMode = function(tab) {
								$scope.novoDSR = null;
								$scope.mode = tab;
								if (tab == 'Pregled')
									$scope.drzavaNM = -1;
							}

							$scope.first = function() {
								$scope.mode = 'Izmena';
								$scope.selectedDSR = $scope.racuni[0];
								$scope.nm = $scope.selectedDSR;
								$scope.novoDSR = $scope.selectedDSR;
							}

							$scope.last = function() {
								$scope.mode = 'Izmena';
								var i = $scope.racuni.length - 1;
								$scope.selectedDSR = $scope.racuni[i];
								$scope.nm = $scope.selectedDSR;
								$scope.novoDSR = $scope.selectedDSR;
							}

							$scope.next = function() {
								$scope.mode = 'Izmena';
								var i = $scope.racuni
										.indexOf($scope.selectedDSR);
								if (i + 1 > $scope.racuni.length - 1)
									$scope.selectedDSR = $scope.racuni[0];
								else
									$scope.selectedDSR = $scope.racuni[i + 1];
								$scope.nm = $scope.selectedDSR;
								$scope.novoDSR = $scope.selectedDSR;
							}

							$scope.prev = function() {
								$scope.mode = 'Izmena';
								var i = $scope.racuni
										.indexOf($scope.selectedDSR);
								if (i == 0)
									$scope.selectedDSR = $scope.racuni[$scope.racuni.length - 1];
								else
									$scope.selectedDSR = $scope.racuni[i - 1];
								$scope.nm = $scope.selectedDSR;
								$scope.novoDSR = $scope.selectedDSR;
							}

							$scope.refreshTable = function() {
								dsrService.svaDSR($rootScope.nextFormRacun.id)
										.then(function(response) {
											if (response.data) {
												$scope.racuni = response.data;
												$scope.novoDSR = null;
												$scope.mode = 'Pregled';
												$scope.selectedDSR = null;
											}
										});
							}

							$scope.odustani = function() {
								$scope.mode = 'Pregled';
								$scope.selectedDSR = null;
								$scope.novoDSR = null;
								$scope.show = null;
							}

						} ]);