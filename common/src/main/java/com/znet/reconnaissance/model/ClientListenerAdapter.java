package com.znet.reconnaissance.model;

public abstract class ClientListenerAdapter implements ClientListener {

	public ClientListenerAdapter() {
		super();
	}
	
	@Override
	public void connected(Client<?> client) {
		// nothing to do
	}

	@Override
	public void registered(Client<?> client) {
		// nothing to do		
	}

	@Override
	public void disconnected(Client<?> client) {
		// nothing to do		
	}
}
