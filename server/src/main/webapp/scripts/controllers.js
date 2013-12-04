(function() {
	angular.module('recon.controllers', [ ])
		.controller('groupsController', [ 
		    '$scope', '$routeParams', 'servicesService', 'websocketService',
		    function($scope, $routeParams, servicesService, websocketService) {
		    	$scope.tree = $routeParams.tree;
				$scope.data = { };
				$scope.groups = servicesService.getGroups($routeParams.tree);
				$scope.notifications = websocketService.subscribe();
		    }
		])
		.controller('serviceController', [ 
		    '$scope', '$routeParams', 'servicesService', 'websocketService',
		    function($scope, $routeParams, servicesService, websocketService) {
		    	$scope.tree = $routeParams.tree;
				$scope.data = { };
				$scope.service = servicesService.getService($routeParams.tree, $routeParams.id);
				$scope.notifications = websocketService.subscribe();
		    }
		]);
})();
