var appControllers = angular.module('appControllers', []);

appControllers.controller('AccountListController', ['$scope', '$http', 
	function($scope, $http) {
		$scope.loading = false;
		
		$scope.load = function() {
			$scope.loading = true;
			$http.get('/api/accounts').
				success(function(data) {
					console.log("Loaded accounts");
					$scope.accounts = data;
					$scope.loading = false;
				}).
				error(function(message) {
					console.error(message);	
					$scope.loading = false;
				});	
		}
		
		$scope.load(); 
	}]);
  
appControllers.controller('AccountDetailController', ['$scope', '$routeParams', '$http', 
	function($scope, $routeParams, $http) {
		$scope.accountIdentifier = $routeParams.accountIdentifier;
		$scope.loading = false;
		
		console.log("AccountIdentifier: ", $scope.accountIdentifier);
		
		$scope.load = function() {
			$scope.loading = true;
			$http.get('/api/accounts/' + $scope.accountIdentifier)
				.success(function(data) {
					console.log("Loaded account details")
					$scope.account = data;
					$scope.loading = false;
				})
				.error(function(message) {
					console.error(message);
					$scope.loading = false;
				});
		}
		
		$scope.load();
	}]);