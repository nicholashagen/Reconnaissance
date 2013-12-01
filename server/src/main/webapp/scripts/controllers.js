(function() {
	angular.module('recon.controllers', [ ])
		.controller('groupsController', [ 
		    '$scope', '$routeParams', 'servicesService',
		    function($scope, $routeParams, servicesService) {
		    	$scope.tree = $routeParams.tree;
				$scope.data = { };
				$scope.groups = servicesService.getGroups($routeParams.tree);
		    }
		])
		.controller('serviceController', [ 
		    '$scope', '$routeParams', 'servicesService',
		    function($scope, $routeParams, servicesService) {
		    	$scope.tree = $routeParams.tree;
				$scope.data = { };
				$scope.service = servicesService.getService($routeParams.tree, $routeParams.id);
		    }
		]);
})();
