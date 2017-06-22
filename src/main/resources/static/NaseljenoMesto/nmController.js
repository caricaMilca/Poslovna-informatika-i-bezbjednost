var app = angular.module('webApp');

app.controller('nmController', [
		'$rootScope',
		'$scope',
		'$location',
		'ngNotify',
		'NMService',
		function($rootScope, $scope, $location, ngNotify, nmService) {


			$scope.mode = 'Pregled';

			if ($rootScope.kojaNM == 'drzave') {
				nmService.svaNMDrzave($rootScope.nextFormDrzava.id).then(
						function(response) {
							if (response.data) {
								//document.getElementById("drzava").selected = $rootScope.nextFormDrzava.zvanicnaSifra;
								$scope.drzavaNM = $rootScope.nextFormDrzava;
								$scope.svaNM = response.data;
								$scope.nmDrzave = $rootScope.nextFormDrzava;
							}
						});
			} else {
				nmService.preuzmiNM().then(function(response) {
					if (response.data) {
						$scope.svaNM = response.data;
					}
				});
			}

			nmService.sveDrzave().then(function(response) {
				if (response.data) {
					$scope.drzave = response.data;
					$scope.drzavaNM = response.data[0];
				}
			});

			$scope.regNM = function() {
				var id;
				if ($scope.drzavaNM == -1)
					id = -1;
				else
					id = $scope.drzavaNM.id;
				if ($scope.mode == 'Dodavanje')
					nmService.regNM($scope.novoNM, $scope.drzavaNM.id).then(
							function(response) {
								if (response.data) {
									ngNotify.set('Uspjesna registracija', {
										type : 'success'
									});
									$scope.svaNM.push(response.data);
									$scope.novoNM = null;
									$scope.show = null;
								}
							});
				else if ($scope.mode == 'Pretraga') {
					nmService.pretraziNM($scope.novoNM, id).then(
							function(response) {
								if (response.data) {
									ngNotify.set('Uspjesna pretraga', {
										type : 'success'
									});
									$scope.svaNM = response.data;
									$scope.novoNM = null;
									$scope.show = null;
								}
							});
				} else if ($scope.mode == 'Izmena') {
					nmService.izmeniNM($scope.novoNM, $scope.drzavaNM.id).then(
							function(response) {
								if (response.data) {
									ngNotify.set('Uspjesna izmena', {
										type : 'success'
									});

									var index = $scope.svaNM
											.indexOf($scope.selectedNM);
									$scope.svaNM[index] = response.data;
									$scope.novoNM = response.data;
									$scope.nm.id = response.data.id;
									$scope.mode = 'Pregled';
								}
							});
				}

			}

			$scope.setSelectedDrzava = function(selected) {
				$scope.drzavaNM = selected;
			}

			$scope.setSelectedNM = function(selected) {
				$scope.selectedNM = selected;
				$scope.show = 10;
				$scope.novoNM = angular.copy(selected);
				$scope.mode = 'Izmena';
				$scope.drzavaNM.sifra = selected.drzava.sifra;
			}

			$scope.changeMode = function(tab) {
				$scope.novoNM = null;
				$scope.mode = tab;
				if (tab == 'Pregled')
					$scope.drzavaNM = -1;
			}

			$scope.izbrisiNM = function() {
				nmService.izbrisiNM($scope.selectedNM.id).then(
						function(response) {
							if (response.status == 200) {
								ngNotify.set('Uspjesno brisanje', {
									type : 'success'
								});
								var index = $scope.svaNM
										.indexOf($scope.selectedNM);
								$scope.svaNM.splice(index, 1);
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
				nmService.preuzmiNM().then(function(response) {
					if (response.data) {
						$scope.svaNM = response.data;
						$scope.novoNM = null;
						$scope.mode = 'Pregled';
						$scope.selectedNM = null;
					}
				});
			}

			$scope.odustani = function() {
				$scope.mode = 'Pregled';
				$scope.selectedNM = null;
				$scope.novoNM = null;
				$scope.show = null;
			}
		} ]);