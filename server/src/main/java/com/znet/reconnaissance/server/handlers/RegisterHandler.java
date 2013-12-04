package com.znet.reconnaissance.server.handlers;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Component;

import com.znet.reconnaissance.handlers.Handler;
import com.znet.reconnaissance.model.Client;
import com.znet.reconnaissance.model.Registration;
import com.znet.reconnaissance.model.RegistrationResponse;
import com.znet.reconnaissance.server.ServiceRegistry;

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
		serviceRegistry.registerService(registration);
        return new RegistrationResponse(registration);
	}
}
