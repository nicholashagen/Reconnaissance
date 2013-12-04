package com.znet.reconnaissance.client;

import java.net.URI;
import java.util.Arrays;


import com.znet.reconnaissance.commands.HeartbeatCommand;
import com.znet.reconnaissance.commands.RegisterCommand;
import com.znet.reconnaissance.handlers.HandlerFactory;
import com.znet.reconnaissance.handlers.HeartbeatHandler;
import com.znet.reconnaissance.model.Callback;
import com.znet.reconnaissance.model.Registration;
import com.znet.reconnaissance.model.Response;

public class Reconnaissance {

	private String serverUrl;
	
	public Reconnaissance(String serverUrl) {
		this.serverUrl = serverUrl;
	}
	
	public static void main(String[] args) throws Exception {
		Registration reg = new Registration();
		reg.setTree("group:subgroup");
		reg.setName("name");
		reg.setType("web");
		reg.setEnvironment("dev");
		reg.setProfiles(Arrays.asList("test", "hsqldb"));
		reg.addMetadata("test", "data");
		
		new Reconnaissance("ws://localhost:8080").register(reg);
	}
	
	public void register(Registration registration) throws Exception {
		
		System.out.println("Initializing...");
		HandlerFactory.register(HeartbeatCommand.NAME, new HeartbeatHandler());
		
		System.out.println("Connecting...");
		URI uri = new URI(String.format("%s/register/websocket", this.serverUrl));

		final WebSocketClient client = new WebSocketClient(uri);
		client.connect();
		System.out.println("Connected");
		
		System.out.println("Registering...");
		client.send(new RegisterCommand(registration), new Callback() {
			
			@Override
			public void process(Response response) {
				System.out.println("Response: " + response.getResult());
			}
		});
	}
}
