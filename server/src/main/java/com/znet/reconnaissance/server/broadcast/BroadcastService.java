package com.znet.reconnaissance.server.broadcast;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.znet.reconnaissance.commands.Command;
import com.znet.reconnaissance.model.Client;
import com.znet.reconnaissance.server.client.ClientRegistry;

@Service
public class BroadcastService {

	@Inject private ClientRegistry clientRegistry;
	
	public void broadcast(Command<?> command) {
		for (Client<?> client : clientRegistry.getClients()) {
			client.send(command);
		}
	}
}
