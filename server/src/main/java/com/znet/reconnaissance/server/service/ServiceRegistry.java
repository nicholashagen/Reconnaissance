package com.znet.reconnaissance.server.service;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Singleton;

import com.znet.reconnaissance.model.Registration;

@org.springframework.stereotype.Service
public class ServiceRegistry {

	private Map<String, Service> services = new ConcurrentHashMap<>();
	
	public ServiceRegistry() {
		super();
	}

	public Collection<Service> listServices() {
		return this.services.values();
	}
	
	public Service registerService(Registration registration) {
		Service service = new Service(registration);
		this.services.put(registration.getId(), service);
		return service;
	}
}
