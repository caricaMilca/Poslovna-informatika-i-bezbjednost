var app = angular.module('webApp');

app
		.controller(
				'medjubankarskiPrenosController',
				[
						'$rootScope',
						'$scope',
						'$location',
						'ngNotify',
						'medjubankarskiPrenosService',
						function($rootScope, $scope, $location, ngNotify,
								medjubankarskiPrenosService) {
				
							$scope.mode = 'Pregled';
							$scope.prenos = null;

							
								medjubankarskiPrenosService.sviMedjubankarskiPrenosi().then(function(response) {
									if (response.data) {
										$scope.sviPrenosi = response.data;									
									}
								});

							
							$scope.setSelectedPrenos = function(selected) {
								$scope.selectedPrenos = selected;
							}

						
							$scope.first = function() {
								$scope.selectedIzvod = $scope.sviPrenosi[0];
								$scope.izvod = $scope.selectedIzvod;
								$scope.noviIzvod = $scope.selectedIzvod;
							}

							$scope.last = function() {
								var i = $scope.sviPrenosi.length - 1;
								$scope.selectedIzvod = $scope.sviPrenosi[i];
								$scope.izvod = $scope.selectedIzvod;
								$scope.noviIzvod = $scope.selectedIzvod;
							}

							$scope.next = function() {
								var i = $scope.sviPrenosi.indexOf($scope.selectedIzvod);
								if (i + 1 > $scope.sviPrenosi.length - 1)
									$scope.selectedIzvod = $scope.sviPrenosi[0];
								else
									$scope.selectedIzvod = $scope.sviPrenosi[i + 1];
								$scope.izvod = $scope.selectedIzvod;
								$scope.noviIzvod = $scope.selectedIzvod;
							}

							$scope.prev = function() {
								var i = $scope.sviPrenosi.indexOf($scope.selectedIzvod);
								if (i == 0)
									$scope.selectedIzvod = $scope.sviPrenosi[$scope.sviPrenosi.length - 1];
								else
									$scope.selectedIzvod = $scope.sviPrenosi[i - 1];
								$scope.izvod = $scope.selectedIzvod;
								$scope.noviIzvod = $scope.selectedIzvod;
							}
							
							$scope.exportMedjubankarskiPrenos = function() {
								 medjubankarskiPrenosService.exportMedjubankarskogPrenosa($scope.selectedPrenos.id).then(function(response) {
										if (response.status == 200) {
											ngNotify
											.set(
													'Uspjesno exportovan medjubankarski prenos',
													{
														type : 'success'
													});
										}
								 }).catch(function(response) {
										ngNotify.set('Ne postoji medjubankarski prenos, ili neka druga greska ' , {
											type : 'error',
										    sticky: true
										});
										console.error('Gists error', response.status, response.data)
									  });
							}
							
							$scope.nextForm = function() {
								$rootScope.kojeAnalitike = 'prenosa';
								$rootScope.nextFormPrenos = $scope.selectedPrenos;
								$location.path('/AnalitikaIzvoda/analitike')
							}
						} ]);