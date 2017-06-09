var app = angular.module('webApp');

app.controller('drzavaController', [
		'$rootScope',
		'$scope',
		'$location',
		'ngNotify',
		'drzavaService',
		function($rootScope, $scope, $location, ngNotify, drzavaService) {

			$scope.mode = 'nulto';
			drzavaService.preuzmiDrzavu().then(function(response) {
				if (response.data) {
					$scope.sveDrzave = response.data;
				}
			});

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
					drzavaService.regDrzavu($scope.novaDrzava,
							$scope.valutaDrzave.id).then(function(response) {
						if (response.data) {
							ngNotify.set('Uspjesna registracija', {
								type : 'success'
							});
							$scope.sveDrzave.push(response.data);
							$scope.novaDrzava = null;
							$scope.show = null;
						}
					});
				else if ($scope.mode == 'filter') {
					drzavaService.pretraziDrzave($scope.novaDrzava, id).then(
							function(response) {
								if (response.data) {
									ngNotify.set('Uspjesna pretraga', {
										type : 'success'
									});
									$scope.sveDrzave = response.data;
									$scope.novaDrzava = null;
									$scope.show = null;
								}
							});
				} else if ($scope.mode == 'edit') {
					drzavaService.izmeniDrzavu($scope.novaDrzava,
							$scope.valutaDrzave.id).then(
							function(response) {
								if (response.data) {
									ngNotify.set('Uspjesna izmena', {
										type : 'success'
									});

									var index = $scope.sveDrzave
											.indexOf($scope.selectedDrzava);
									$scope.sveDrzave[index] = response.data;
									$scope.novaDrzava = response.data;
									$scope.nm.id = response.data.id;
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
				drzavaService.izbrisiDrzavu($scope.selectedDrzava.id).then(
						function(response) {
							if (response.status == 200) {
								ngNotify.set('Uspjesno brisanje', {
									type : 'success'
								});
								var index = $scope.sveDrzave
										.indexOf($scope.selectedDrzava);
								$scope.sveDrzave.splice(index, 1);
								$scope.novaDrzava = null;
								$scope.show = null;
								$scope.selectedDrzava = null;
							}

						});
			}

		} ]);