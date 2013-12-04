package com.znet.reconnaissance.server;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.znet.reconnaissance.model.Registration;

@Service
public class ServiceRegistry {

	private Set<Registration> services = 
		new HashSet<Registration>();
	
	public ServiceRegistry() {
		super();
	}

	public Set<Registration> listServices() {
		return this.services;
	}
	
	public void registerService(Registration registration) {
		this.services.add(registration);
	}
}
