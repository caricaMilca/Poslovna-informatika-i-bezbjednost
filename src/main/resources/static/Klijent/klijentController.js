var app = angular.module('webApp');

app.run([ 'ngNotify', function(ngNotify) {

	ngNotify.config({
		position : 'bottom',
		duration : 1000,
		theme : 'pitchy',
		sticky : false,
	});
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

							$scope.regKlijentaP = function() {
								if ($scope.mode == 'add') 
									klijentService
											.regKlijentaP(
													$scope.novi,
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
															$scope.novi = null;
															$scope.show = null;
														}
													});
								else if ($scope.mode == 'filter') {
									var id;
									if ($scope.djelatnostKlijenta == undefined)
										id = -1;
									else
										id = $scope.djelatnostKlijenta.id;
									klijentService
											.pretraziKlijente($scope.novi, id)
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
															$scope.novi = null;
															$scope.show = null;
														}
													});
								}
							}

							$scope.regKlijentaF = function() {
								if ($scope.mode == 'add')
									klijentService
											.regKlijentaF($scope.novi)
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
															$scope.novi = null;
															$scope.show = null;
														}
													});
								else if ($scope.mode == 'filter') {
									var id;
									if ($scope.djelatnostKlijenta == undefined)
										id = -1;
									else
										id = $scope.djelatnostKlijenta.id;
									klijentService
											.pretraziKlijente($scope.novi, id)
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
															$scope.novi = null;
															$scope.show = null;
														}

													});
								}
							}

							$scope.setSelectedDjelatnost = function(selected) {
								$scope.djelatnostKlijenta = selected;
							}

							$scope.setSelectedKlijent = function(selected) {
								$scope.selectedKlijent = selected;
								$scope.show = 10;
								$scope.novi = angular.copy(selected);
								$scope.mode = 'edit';

							}

							$scope.display = function(tab) {
								$scope.novi = null;
								$scope.show = tab;
								if (tab == 10)
									$scope.mode = 'add';
								else if (tab == 1)
									$scope.mode = 'filter';
								else
									$scope.mode = 'edit';
							}

						} ]);