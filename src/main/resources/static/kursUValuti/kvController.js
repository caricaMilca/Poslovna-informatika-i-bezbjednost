var app = angular.module('webApp');

app.controller('kvController', [
		'$rootScope',
		'$scope',
		'$location',
		'ngNotify',
		'KvService',
		function($rootScope, $scope, $location, ngNotify, kvService) {

			$scope.mode = 'nulto';
			kvService.preuzmiV().then(function(response) {
				if (response.data) {
					$scope.sveV = response.data;
				}
			});

			kvService.sveKursneListe().then(function(response) {
				if (response.data) {
					$scope.sveListe = response.data;
					$scope.kl = response.data[0];
				}
			});

			kvService.sveValute().then(function(response) {
				if (response.data) {
					$scope.valute = response.data;
					$scope.valutaK = response.data[0];
					$scope.valutaK2 = response.data[0];
				}
			});

			$scope.regV = function() {
				var id;
				if ($scope.valutaK == -1)
					id = -1;
				else
					id = $scope.valutaK.id;
				
				var id2;
				if ($scope.valutaK2 == -1)
					id2 = -1;
				else
					id2 = $scope.valutaK2.id;
				
				var id3;
				if ($scope.kl == -1)
					id3 = -1;
				else
					id3 = $scope.kl.id;
								
				if ($scope.mode == 'add')
					kvService.regV($scope.novaV,$scope.valutaK.id,$scope.valutaK2.id,$scope.kl.id).then(
							function(response) {
								if (response.data) {
									ngNotify.set('Uspesno dodavanje', {
										type : 'success'
									});
									$scope.sveV.push(response.data);
									$scope.novaV = null;
									$scope.show = null;
								}
							});
				else if ($scope.mode == 'filter') {
					kvService.pretraziV($scope.novaV)
							.then(function(response) {
								if (response.data) {
									ngNotify.set('Uspesna pretraga', {
										type : 'success'
									});
									$scope.sveV = response.data;
									$scope.novaV = null;
									$scope.show = null;
								}
							});
				} else if ($scope.mode == 'edit') {
					kvService.izmeniV($scope.novaV,$scope.valutaK.id,$scope.valutaK2.id,$scope.kl.id).then(
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
									$scope.mode = 'nulto';
								}
							});
				}

			}
			
			$scope.setSelectedValuta = function(selected) {
				$scope.valutaK = selected;
			}
			
			$scope.setSelectedValuta2 = function(selected) {
				$scope.valutaK2 = selected;
			}
			
			$scope.setSelectedLista = function(selected) {
				$scope.kl = selected;
			}

			$scope.setSelectedV = function(selected) {
				$scope.selectedV = selected;
				$scope.show = 10;
				$scope.novaV = angular.copy(selected);
				$scope.mode = 'edit';
				$scope.valutaK.naziv = selected.premaValuti.naziv;
				$scope.valutaK2.naziv = selected.osnovnaValuta.naziv;
				$scope.kl.broj = selected.kursnaLista.broj;
			}

			$scope.changeMode = function(tab) {
				$scope.novaV = null;
				$scope.mode = tab;
			}
			
			$scope.izbrisiV = function() {
				kvService
						.izbrisiV(
								$scope.selectedV.id)
						.then(
								function(response) {
									if (response.status == 200) {
										ngNotify
												.set(
														'Uspesno brisanje',
														{
															type : 'success'
														});
										var index = $scope.sveV
												.indexOf($scope.selectedV);
										$scope.sveV
												.splice(index,
														1);
										$scope.novaV = null;
										$scope.show = null;
										$scope.selectedV = null;
									}

								});
			}
			
			
			$scope.first = function() {
				$scope.mode = 'edit';
				$scope.selectedV = $scope.sveV[0];
				$scope.v = $scope.selectedV;
				$scope.novaV = $scope.selectedV;
			}

			$scope.last = function() {
				$scope.mode = 'edit';
				var i = $scope.sveV.length - 1;
				$scope.selectedV = $scope.sveV[i];
				$scope.v = $scope.selectedV;
				$scope.novaV = $scope.selectedV;
			}

			$scope.next = function() {
				$scope.mode = 'edit';
				var i = $scope.sveV
						.indexOf($scope.selectedV);
				if (i + 1 > $scope.sveV.length - 1)
					$scope.selectedV = $scope.sveV[0];
				else
					$scope.selectedV = $scope.sveV[i + 1];
				$scope.v = $scope.selectedV;
				$scope.novaV = $scope.selectedV;
			}

			$scope.prev = function() {
				$scope.mode = 'edit';
				var i = $scope.sveV
						.indexOf($scope.selectedV);
				if (i == 0)
					$scope.selectedV = $scope.sveV[$scope.sveV.length - 1];
				else
					$scope.selectedV = $scope.sveV[i - 1];
				$scope.v = $scope.selectedV;
				$scope.novaV = $scope.selectedV;
			}

			$scope.refreash = function() {
				kvService
						.preuzmiV()
						.then(
								function(response) {
									if (response.data) {
										$scope.sveV = response.data;
										$scope.novaV = null;
										$scope.mode = 'nulto';
										$scope.selectedV = null;
									}
								});
			}


			$scope.odustani = function() {
				$scope.mode = 'nulto';
				$scope.selectedV = null;
				$scope.novaV = null;
				$scope.show = null;
			}
			
		} ]);