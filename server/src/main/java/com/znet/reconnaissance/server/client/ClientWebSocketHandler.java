package com.znet.reconnaissance.server.client;

import javax.inject.Inject;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.adapter.TextWebSocketHandlerAdapter;

import com.znet.reconnaissance.model.Client;
import com.znet.reconnaissance.server.service.websockets.WSClient;

@Component
public class ClientWebSocketHandler extends TextWebSocketHandlerAdapter {

	@Inject private ClientRegistry clientRegistry;
	
	@Override
    public void afterConnectionEstablished(final WebSocketSession session) 
    		throws Exception {
		
		WSClient client = new WSClient(session);
		client.connected();
		
		clientRegistry.registerClient(client);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message)
    		throws Exception {
    	String payload = message.getPayload();
    	System.out.println("WEB SOCKET MSG:  " + session + ": " + payload);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status)
    		throws Exception {
    	
    	Client<?> client = clientRegistry.getClient(session.getId());
    	client.disconnect();
    }
}
