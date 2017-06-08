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

app.controller('nmController', [
		'$rootScope',
		'$scope',
		'$location',
		'ngNotify',
		'NMService',
		function($rootScope, $scope, $location, ngNotify, nmService) {

			$scope.mode = 'nulto';
			nmService.preuzmiNM().then(function(response) {
				if (response.data) {
					$scope.svaNM = response.data;
				}
			});

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
				if ($scope.mode == 'add')
					nmService.regNM($scope.novoNM,
							$scope.drzavaNM.id).then(
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
				else if ($scope.mode == 'filter') {
					nmService.pretraziNM($scope.novoNM, id)
							.then(function(response) {
								if (response.data) {
									ngNotify.set('Uspjesna pretraga', {
										type : 'success'
									});
									$scope.svaNM = response.data;
									$scope.novoNM = null;
									$scope.show = null;
								}
							});
				} else if ($scope.mode == 'edit') {
					nmService.izmeniNM($scope.novoNM,
							$scope.drzavaNM.id).then(
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
									$scope.mode = 'nulto';
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
				$scope.mode = 'edit';
				$scope.drzavaNM.sifra = selected.drzava.sifra;
			}

			$scope.changeMode = function(tab) {
				$scope.novoNM = null;
				$scope.mode = tab;
				if (tab == 'filter')
					$scope.drzavaNM = -1;
			}
			
			$scope.izbrisiNM = function() {
				nmService
						.izbrisiNM(
								$scope.selectedNM.id)
						.then(
								function(response) {
									if (response.status == 200) {
										ngNotify
												.set(
														'Uspjesno brisanje',
														{
															type : 'success'
														});
										var index = $scope.svaNM
												.indexOf($scope.selectedNM);
										$scope.svaNM
												.splice(index,
														1);
										$scope.novoNM = null;
										$scope.show = null;
										$scope.selectedNM = null;
									}

								});
			}

		} ]);