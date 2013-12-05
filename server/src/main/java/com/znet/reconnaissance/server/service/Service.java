package com.znet.reconnaissance.server.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.znet.reconnaissance.model.Client;
import com.znet.reconnaissance.model.Registration;
import com.znet.reconnaissance.server.websockets.ServiceWebSocketHandler.WSClient;

public class Service {

	private String id;
	private transient Client<?> client;
	private Registration registration;
	
	public Service(Registration registration) {
		this.id = registration.getId();
		this.registration = registration;
	}
	
	public String getId() { return this.id; }
	
	@JsonIgnore
	public Client<?> getClient() { return this.client; }
	
	public Registration getRegistration() { return this.registration; }
	
	// TODO: fix circular ref between WSClient and Service
	
	public void disconnected() {
		((WSClient) this.client).setService(null);
		this.client = null;
	}
	
	public void connected(Client<?> client) { 
		this.client = client;
		((WSClient) this.client).setService(this);
	}
	
	public boolean isActive() {
		return this.client != null;
	}
}
