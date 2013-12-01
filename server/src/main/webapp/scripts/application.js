(function(window) {
	var application = window.reconnaissance = angular.module('recon', [ 
        'ngRoute', 'ngResource', 'jmdobry.angular-cache',
        'recon.models', 'recon.services', 'recon.controllers' 
    ]);
	
	// REFACTOR OUT TO OWN MODULE
	
	
	window.reconnaissance.clearCache = function() {
		var $angularCacheFactory =
			angular.injector([ 'recon', 'ng' ]).get('$angularCacheFactory');

		var httpCache = $angularCacheFactory.get('httpCache');
		httpCache.removeAll();
	}
	
	application.run([ 
	    '$http', '$angularCacheFactory', 
	    function($http, $angularCacheFactory) {
	    	$angularCacheFactory('httpCache', {
	    		capacity: 100,
	            maxAge: 2, // 900000, // 15 mins
	            deleteOnExpire: 'passive', // 'aggressive',
	            recycleFreq: 60000, // 1 min
	            // cacheFlushInterval: 3600000, // 1 hr
	            storageMode: 'localStorage',
	            // storageImpl: myLocalStoragePolyfill,
	            verifyIntegrity: true,
	            onExpire: function (key, value) {

	            }
	        });

	    	var httpCache = $angularCacheFactory.get('httpCache');
	    	
	    	var cache = {
    	    	put : function (key, value) {
    	    		var options = { };
    	    		if (angular.isArray(value) && typeof value[2] === 'object') {
    	    			var headers = value[2];
    	    			if (headers['max-age']) {
    	    				options.maxAge = parseInt(headers['max-age'], 10);
    	    			}
    	    		}
    	    		
    	    		return httpCache.put(key, value, options);
    	    	},

    	    	get : function (key) {
    	    		return httpCache.get(key);
    	    	}
	    	};
	    	
	    	var wrapAll = function(names) {
	    		for (var i = 0; i < names.length; i++) {
	    			wrap(names[i]);
	    		}
	    	}
	    	
	    	var wrap = function(name) {
    			cache[name] = function() {
    				httpCache[name].apply(httpCache, arguments);
    			}
	    	}
    	    
	    	wrapAll([ 
	    	    'remove', 'removeAll', 'destroy', 'info', 'keySet', 'keys' 
	    	]);
    	
	        $http.defaults.cache = cache;
	    }
	]);
	
	application.run([ 
        'servicesService', 
        function(servicesService) {
        	servicesService.getGroups();
        }
    ]);
	
	application.config([
	    '$routeProvider', '$locationProvider',
	    function($routeProvider, $locationProvider) {
	    	$locationProvider.html5Mode(false);
		    $routeProvider
		    .when('/groups/:tree*', {
	    		templateUrl: 'partials/index.html',
	    		controller: 'groupsController'
		    })
		    .when('/services/:tree*/:id', {
	    		templateUrl: 'partials/service.html',
	    		controller: 'serviceController'
		    })
		    .otherwise({
		    	templateUrl: 'partials/index.html',
	    		controller: 'groupsController'
		    });
	    }
	]);
})(window);
