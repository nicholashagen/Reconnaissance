(function() {
	angular.module('recon.services', [ ])
		.service('servicesService', [
		    'Service', '$rootScope',
		    function(Service) {
		    	return {
		    		getService : function(tree, id) {
		    			var service = { foo: 'bar' };
		    			this.getGroups(tree, function(groups) {
		    				angular.forEach(groups.services, function(value) {
		    					if (value.name === id) {
		    						angular.forEach(value, function(v, k) {
		    							service[k] = v;
		    						});
		    					}
		    				});
		    			});
		    			
		    			return service;
		    		},
		    		getGroups : function(id, callback) {
		    			var results = { 
		    				groups: [ ],
		    				services: [ ]
		    			};
		    			
		    			var groups = { children: { }, services: { } };
		    			Service.query(function(services) {
		    				for (var i = 0; i < services.length; i++) {
			    				var service = services[i];
			    				var tree = service.tree.split(/:/);
			    				
			    				var group = groups;
			    				for (var j = 0; j < tree.length; j++) {
			    					var tmp = group.children[tree[j]];
			    					if (!tmp) {
			    						tmp = { children: { }, services: { } };
			    						group.children[tree[j]] = tmp;
			    					}
			    					
			    					group = tmp;
			    				}
			    				
			    				var tmp = group.services[service.name];
			    				if (!tmp) {
			    					tmp = { 
			    						name: service.name,
			    						type: service.type,
			    						instances: [ ] 
			    					};
			    					group.services[service.name] = tmp;
			    				}
			    				
			    				tmp.instances.push(service);
			    			}
		    				
		    				if (id) {
			    				var ids = id.split(/\//);
			    				for (var i = 0; i < ids.length; i++) {
			    					groups = groups.children[ids[i]];
			    					if (!groups) { break; }
			    				}
		    				}
		    				
		    				if (groups) {
		    					angular.forEach(groups.children, function(value, key) {
			    					results.groups.push({ id: key });
			    				});
		    					
		    					angular.forEach(groups.services, function(value, key) {
		    						results.services.push(value);
		    					});
		    				}

		    				results.groupsValid = results.groups.length > 0;
			    			results.servicesValid = results.services.length > 0;
			    			results.groupsActive = results.groupsValid;
			    			results.servicesActive = !results.groupsValid && results.servicesValid;
			    			
			    			if (typeof callback === 'function') {
			    				callback(results);
			    			}
		    			});
		    			
		    			return results;
		    		}
		    	}
		    }
		]);
})();
