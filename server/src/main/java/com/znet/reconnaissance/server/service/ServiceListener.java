package com.znet.reconnaissance.server.service;

public interface ServiceListener {

	public void connected(Service service);
	
	public void registered(Service service);
	
	public void disconnected(Service service);
}
