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
				error(function(message, code) {
					console.error("Error loading accounts: ", code);
					$scope.accounts = null;
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
				.error(function(message, code) {
					console.error("Error loading account details: ", code);
					$scope.account = null;
					$scope.loading = false;
				});
		}
		
		$scope.load();
	}]);
	
appControllers.controller('UserController', ['$scope', '$http', 
	function($scope, $http) {
		$scope.loggedIn = false;
		
		$http.get('/api/users')
			.success(function(data) {
				$scope.user = data;
				$scope.loggedIn = true;
			})
			.error(function(message, code) {
				console.error("Error loading user: ", code);
				$scope.user = null;
				$scope.loggedIn = false;
			});
	}])