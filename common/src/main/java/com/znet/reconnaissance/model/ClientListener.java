package com.znet.reconnaissance.model;

public interface ClientListener {

	public void connected(Client<?> client);
	
	public void registered(Client<?> client);
	
	public void disconnected(Client<?> client);
}
