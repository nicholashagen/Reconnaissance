(function() {
	angular.module('recon.models', [ ])
		.factory('Service', [ 
		    '$resource',
		    function($resource) {
				return $resource('/services/:id', { }, {
					loadGroups : { method: 'GET', isArray: true }
				});
		    }
		]);
})();
