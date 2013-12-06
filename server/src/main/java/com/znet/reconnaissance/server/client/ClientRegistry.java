package com.znet.reconnaissance.server.client;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.znet.reconnaissance.model.Client;
import com.znet.reconnaissance.model.ClientListener;
import com.znet.reconnaissance.model.ClientListenerAdapter;

@Service
public class ClientRegistry {

	private Map<String, Client<?>> clients = new ConcurrentHashMap<>();
	private ClientListener clientListener = new ClientListenerHandler();
	
	public ClientRegistry() {
		super();
	}
	
	public Client<?> getClient(String id) {
		return this.clients.get(id);
	}
	
	public Collection<Client<?>> getClients() {
		return this.clients.values();
	}
	
	public boolean registerClient(Client<?> client) {
		
		// save client
		this.clients.put(client.getId(), client);
		
		// add callback to update
		client.addListener(clientListener);
		
		// success
		return true;
	}
	
	public class ClientListenerHandler extends ClientListenerAdapter {
		@Override
		public void disconnected(Client<?> client) {
			clients.remove(client.getId());
		}
	}
}
