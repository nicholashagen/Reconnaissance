package com.znet.reconnaissance.server.notifications;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.znet.reconnaissance.model.Client;
import com.znet.reconnaissance.server.client.ClientRegistry;

@Service
public class NotificationService {

	@Inject private ClientRegistry clientRegistry;
	
	public void notify(Notification notification) {
		NotificationCommand command = new NotificationCommand(notification); 
		for (Client<?> client : clientRegistry.getClients()) {
			client.send(command);
		}
	}
}
