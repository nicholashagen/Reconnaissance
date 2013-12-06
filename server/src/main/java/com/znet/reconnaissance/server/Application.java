package com.znet.reconnaissance.server;

import javax.inject.Inject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.socket.WebSocketHandler;

import com.znet.reconnaissance.server.client.ClientWebSocketHandler;
import com.znet.reconnaissance.server.service.websockets.ServiceWebSocketHandler;

@ComponentScan
@EnableAutoConfiguration
public class Application {

	@Inject private ClientWebSocketHandler clientWebSocketHandler;
	@Inject private ServiceWebSocketHandler serviceWebSocketHandler;
	
	public static void main(String... args) {
		SpringApplication.run(Application.class, args);

	}
	
	@Bean(name="/register")
    public WebSocketHandler createServiceWebSocketHandler() {
		return serviceWebSocketHandler;
    }
	
	@Bean(name="/status")
    public WebSocketHandler createClientWebSocketHandler() {
		return clientWebSocketHandler;
    }
}
