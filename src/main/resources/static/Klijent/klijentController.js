var app = angular.module('webApp');

app.run([ 'ngNotify', function(ngNotify) {

	ngNotify.config({
		position : 'bottom',
		duration : 1000,
		theme : 'pitchy',
		sticky : false,
	});
} ]);

app.directive('ngConfirmClick', [ function() {
	return {
		link : function(scope, element, attr) {
			var msg = attr.ngConfirmClick || "Are you sure?";
			var clickAction = attr.confirmedClick;
			element.bind('click', function(event) {
				if (window.confirm(msg)) {
					scope.$eval(clickAction)
				}
			});
		}
	};
} ]);

app.config([ '$qProvider', function($qProvider) {
	$qProvider.errorOnUnhandledRejections(false);
} ]);

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

							$scope.mode = 'nulto';
							klijentService.preuzmiKlijente().then(
									function(response) {
										if (response.data) {
											$scope.sviKlijenti = response.data;
										}
									});
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
								if ($scope.mode == 'add')
									klijentService
											.regKlijenta(
													$scope.noviKlijent,
													$scope.djelatnostKlijenta.id)
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
															$scope.mode = 'nulto';
															$scope.selectedKlijent = null;
														}
													});
								else if ($scope.mode == 'filter') {
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
															$scope.mode = 'nulto';
															$scope.selectedKlijent = null;
														}
													});
								} else if ($scope.mode == 'edit') {
									klijentService
											.izmeniKlijenta(
													$scope.noviKlijent,
													$scope.djelatnostKlijenta.id)
											.then(
													function(response) {
														if (response.data) {
															ngNotify
																	.set(
																			'Uspjesna izmena',
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
														$scope.mode = 'nulto';
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
								$scope.mode = 'edit';
								if (selected.ulogaK == 'POSLOVNO')
									$scope.djelatnostKlijenta.sifra = selected.djelatnost.sifra;
							}

							$scope.changeMode = function(tab) {
								$scope.noviKlijent = null;
								$scope.mode = tab;
								if (tab == 'filter')
									$scope.djelatnostKlijenta = -1;
							}

							$scope.izbrisiKlijenta = function(tabela) {
								if (tabela == 'klijenti')
									klijentService
											.preuzmiKlijente()
											.then(
													function(response) {
														if (response.data) {
															$scope.sviKlijenti = response.data;
															$scope.mode = 'nulto';
															$scope.selectedKlijent = null;
														}
													});
							}

							$scope.first = function() {
								$scope.mode = 'edit';
								$scope.selectedKlijent = $scope.sviKlijenti[0];
								$scope.k = $scope.selectedKlijent;
								$scope.noviKlijent = $scope.selectedKlijent;
							}

							$scope.last = function() {
								$scope.mode = 'edit';
								var i = $scope.sviKlijenti.length - 1;
								$scope.selectedKlijent = $scope.sviKlijenti[i];
								$scope.k = $scope.selectedKlijent;
								$scope.noviKlijent = $scope.selectedKlijent;
							}

							$scope.next = function() {
								$scope.mode = 'edit';
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
								$scope.mode = 'edit';
								var i = $scope.sviKlijenti
										.indexOf($scope.selectedKlijent);
								if (i == 0)
									$scope.selectedKlijent = $scope.sviKlijenti[$scope.sviKlijenti.length - 1];
								else
									$scope.selectedKlijent = $scope.sviKlijenti[i - 1];
								$scope.k = $scope.selectedKlijent;
								$scope.noviKlijent = $scope.selectedKlijent;
							}

							$scope.refreashTable = function() {
								klijentService
										.preuzmiKlijente()
										.then(
												function(response) {
													if (response.data) {
														$scope.sviKlijenti = response.data;
														$scope.noviKlijent = null;
														$scope.mode = 'nulto';
														$scope.selectedKlijent = null;
													}
												});
							}

						} ]);