var appDirectChallengeApp = angular.module('appDirectChallengeApp', [
	'ngRoute',
	'appControllers'
]);

appDirectChallengeApp.config(['$routeProvider',
	function($routeProvider) {
		$routeProvider.
			when('/', {
				templateUrl: 'app/partials/account-list.htm',
				controller: 'AccountListController'
			}).
			when('/account/:accountIdentifier', {
				templateUrl: 'app/partials/account-detail.htm',
				controller: 'AccountDetailController'
			}).
			otherwise({
				redirectTo: '/'
			});
	}]);