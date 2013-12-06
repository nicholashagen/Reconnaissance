package com.znet.reconnaissance.server.handlers;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Component;

import com.znet.reconnaissance.handlers.Handler;
import com.znet.reconnaissance.model.Client;
import com.znet.reconnaissance.model.ClientListenerAdapter;
import com.znet.reconnaissance.model.Registration;
import com.znet.reconnaissance.model.RegistrationResponse;
import com.znet.reconnaissance.server.service.Service;
import com.znet.reconnaissance.server.service.ServiceRegistry;
import com.znet.reconnaissance.server.service.websockets.WSService;

@Component
@Named("register")
public class RegisterHandler 
	implements Handler<Registration, RegistrationResponse> {

	@Inject
	private ServiceRegistry serviceRegistry;
	
	public RegisterHandler() {
		super();
	}
	
	public RegistrationResponse process(Client<?> client,
			Registration registration) {
		
		final Service service = new WSService(client, registration);
		client.addListener(new ClientListenerAdapter() {
			
			@Override
			public void disconnected(Client<?> client) {
				service.disconnected();
			}
		});

		if (serviceRegistry.registerService(service)) {
			client.registered();
		}
		
		// TODO: get plugins and send down
        return new RegistrationResponse(registration);
	}
}
