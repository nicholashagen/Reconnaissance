package com.znet.reconnaissance.server.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class Service {

	private String id;
	private Set<ServiceListener> listeners = new HashSet<>();
	
	public Service(String id) {
		this.id = id;
	}
	
	public String getId() { 
		return this.id; 
	}
	
	public synchronized void addListener(ServiceListener listener) {
		this.listeners.add(listener);
	}
	
	public synchronized void removeListener(ServiceListener listener) {
		this.listeners.remove(listener);
	}
	
	public synchronized void connected() {
		for (ServiceListener listener : this.listeners) {
			listener.connected(this);
		}
	}
	
	public synchronized void registered() {
		for (ServiceListener listener : this.listeners) {
			listener.registered(this);
		}
	}
	
	public synchronized void disconnected() {
		for (ServiceListener listener : this.listeners) {
			listener.disconnected(this);
		}
	}
	
	public void disconnect() {
		disconnected();
	}
	
	public abstract boolean isConnected();
	
	public abstract boolean heartbeat();
	
	public abstract String getTree();
	public abstract String getName();
	public abstract String getType();
	public abstract List<String> getProfiles();
	public abstract String getHostname();
	public abstract String getEnvironment();
	public abstract Map<String, String> getMetadata();	
}
