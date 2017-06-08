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

app.controller('valuteController', [
		'$rootScope',
		'$scope',
		'$location',
		'ngNotify',
		'ValuteService',
		function($rootScope, $scope, $location, ngNotify, valuteService) {

			$scope.mode = 'nulto';
			valuteService.preuzmiV().then(function(response) {
				if (response.data) {
					$scope.sveV = response.data;
				}
			});


			$scope.regV = function() {
					if ($scope.mode == 'add')
					valuteService.regV($scope.novaV).then(
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
					valuteService.pretraziV($scope.novaV)
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
									$scope.mode = 'nulto';
								}
							});
				}

			}

			$scope.setSelectedV = function(selected) {
				$scope.selectedV = selected;
				$scope.show = 10;
				$scope.novaV = angular.copy(selected);
				$scope.mode = 'edit';
			}

			$scope.changeMode = function(tab) {
				$scope.novaV = null;
				$scope.mode = tab;
			}
			
			$scope.izbrisiNM = function() {
				valuteService
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
										var index = $scope.svaV
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

		} ]);