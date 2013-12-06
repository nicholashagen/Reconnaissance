package com.znet.reconnaissance.server.service.websockets;

import java.util.List;
import java.util.Map;

import com.znet.reconnaissance.model.Client;
import com.znet.reconnaissance.model.Registration;
import com.znet.reconnaissance.server.service.Service;

public class WSService extends Service {

	private Client<?> client;
	private Registration registration;
	
	public WSService(Client<?> client, Registration registration) {
		super(client.getId());
		
		this.client = client;
		this.registration = registration;
	}
	
	@Override
	public void disconnect() {
		this.client.disconnect();
	}
	
	@Override
	public boolean heartbeat() {
		if (!isConnected()) { return false; }
		return this.client.heartbeat();
	}
	
	@Override
	public String getTree() {
		return this.registration.getTree();
	}

	@Override
	public String getName() {
		return this.registration.getName();
	}

	@Override
	public String getType() {
		return this.registration.getType();
	}

	@Override
	public List<String> getProfiles() {
		return this.registration.getProfiles();
	}
	
	@Override
	public String getHostname() {
		return this.registration.getHostname();
	}
	
	@Override
	public String getEnvironment() {
		return this.registration.getEnvironment();
	}

	@Override
	public Map<String, String> getMetadata() {
		return this.registration.getMetadata();
	}
	
	@Override
	public boolean isConnected() {
		return this.client != null && this.client.isConnected();
	}
}
