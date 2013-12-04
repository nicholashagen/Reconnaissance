package com.znet.reconnaissance.server;

import javax.inject.Inject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.support.PerConnectionWebSocketHandler;

import com.znet.reconnaissance.server.websockets.ServiceWebSocketHandler;
import com.znet.reconnaissance.server.websockets.ClientWebSocketHandler;

@ComponentScan
@EnableAutoConfiguration
public class Application {

	@Inject
	private ServiceWebSocketHandler serviceWebSocketHandler;
	
	public static void main(String... args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean(name="/register")
    public WebSocketHandler createServiceWebSocketHandler() {
		return serviceWebSocketHandler;
    }
	
	@Bean(name="/status")
    public WebSocketHandler createClientWebSocketHandler() {
		return new PerConnectionWebSocketHandler(ClientWebSocketHandler.class);
    }
}
