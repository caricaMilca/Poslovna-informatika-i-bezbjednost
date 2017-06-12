var app = angular.module('webApp');

app.controller('valuteController', [
		'$rootScope',
		'$scope',
		'$location',
		'ngNotify',
		'ValuteService',
		function($rootScope, $scope, $location, ngNotify, valuteService) {

			$rootScope.kojiKursevi = '';
			$rootScope.kojiKlijenti = 'svi';
			$rootScope.kojaNM = ''
			$scope.mode = 'Pregled';
			valuteService.preuzmiV().then(function(response) {
				if (response.data) {
					$scope.sveV = response.data;
				}
			});

			$scope.regV = function() {
				if ($scope.mode == 'Dodavanje')
					valuteService.regV($scope.novaV).then(function(response) {
						if (response.data) {
							ngNotify.set('Uspesno dodavanje', {
								type : 'success'
							});
							$scope.sveV.push(response.data);
							$scope.novaV = null;
							$scope.show = null;
						}
					});
				else if ($scope.mode == 'Pretraga') {
					valuteService.pretraziV($scope.novaV).then(
							function(response) {
								if (response.data) {
									ngNotify.set('Uspesna pretraga', {
										type : 'success'
									});
									$scope.sveV = response.data;
									$scope.novaV = null;
									$scope.show = null;
								}
							});
				} else if ($scope.mode == 'Izmena') {
					valuteService.izmeniV($scope.novaV).then(
							function(response) {
								if (response.data) {
									ngNotify.set('Uspesna izmena', {
										type : 'success'
									});

									var index = $scope.sveV
											.indexOf($scope.selectedV);
									$scope.sveV[index] = response.data;
									$scope.novaV = response.data;
									$scope.v.id = response.data.id;
									$scope.mode = 'Pregled';
								}
							});
				}

			}

			$scope.setSelectedV = function(selected) {
				$scope.selectedV = selected;
				$scope.show = 10;
				$scope.novaV = angular.copy(selected);
				$scope.mode = 'Izmena';
				$scope.novaV.domicilna = selected.domicilna;
			}

			$scope.changeMode = function(tab) {
				$scope.novaV = null;
				$scope.mode = tab;
			}

			$scope.izbrisiV = function() {
				valuteService.izbrisiV($scope.selectedV.id).then(
						function(response) {
							if (response.status == 200) {
								ngNotify.set('Uspesno brisanje', {
									type : 'success'
								});
								var index = $scope.sveV
										.indexOf($scope.selectedV);
								$scope.sveV.splice(index, 1);
								$scope.novaV = null;
								$scope.show = null;
								$scope.selectedV = null;
							}

						});
			}

			$scope.first = function() {
				$scope.mode = 'Izmena';
				$scope.selectedV = $scope.sveV[0];
				$scope.v = $scope.selectedV;
				$scope.novaV = $scope.selectedV;
			}

			$scope.last = function() {
				$scope.mode = 'Izmena';
				var i = $scope.sveV.length - 1;
				$scope.selectedV = $scope.sveV[i];
				$scope.v = $scope.selectedV;
				$scope.novaV = $scope.selectedV;
			}

			$scope.next = function() {
				$scope.mode = 'Izmena';
				var i = $scope.sveV.indexOf($scope.selectedV);
				if (i + 1 > $scope.sveV.length - 1)
					$scope.selectedV = $scope.sveV[0];
				else
					$scope.selectedV = $scope.sveV[i + 1];
				$scope.v = $scope.selectedV;
				$scope.novaV = $scope.selectedV;
			}

			$scope.prev = function() {
				$scope.mode = 'Izmena';
				var i = $scope.sveV.indexOf($scope.selectedV);
				if (i == 0)
					$scope.selectedV = $scope.sveV[$scope.sveV.length - 1];
				else
					$scope.selectedV = $scope.sveV[i - 1];
				$scope.v = $scope.selectedV;
				$scope.novaV = $scope.selectedV;
			}

			$scope.refreash = function() {
				valuteService.preuzmiV().then(function(response) {
					if (response.data) {
						$scope.sveV = response.data;
						$scope.novaV = null;
						$scope.mode = 'Pregled';
						$scope.selectedV = null;
					}
				});
			}

			$scope.odustani = function() {
				$scope.mode = 'Pregled';
				$scope.selectedV = null;
				$scope.novaV = null;
				$scope.show = null;
			}

			$scope.prikaziDrzave = function() {
				$('#izaberiNextFormu').modal('hide');
				$rootScope.kojeDrzave = 'valute';
				$rootScope.nextFormValuta = $scope.selectedV;
				$location.path('/Drzava/sveDrzave')
			}

			$scope.prikaziKurseve = function() {
				$('#izaberiNextFormu').modal('hide');
				$rootScope.kojiKursevi = 'valute';
				$rootScope.nextFormValuta = $scope.selectedV;
				$location.path('/kursUValuti/kurs')
			}

		} ]);