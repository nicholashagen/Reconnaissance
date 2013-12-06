package com.znet.reconnaissance.server.service;

public abstract class ServiceListenerAdapter implements ServiceListener {

	public ServiceListenerAdapter() {
		super();
	}

	@Override
	public void connected(Service service) {
		// nothing to do
	}

	@Override
	public void disconnected(Service service) {
		// nothing to do		
	}
}
