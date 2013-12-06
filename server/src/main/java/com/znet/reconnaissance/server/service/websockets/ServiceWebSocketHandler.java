package com.znet.reconnaissance.server.service.websockets;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.adapter.TextWebSocketHandlerAdapter;

import com.znet.reconnaissance.handlers.Handler;
import com.znet.reconnaissance.handlers.HandlerFactory;
import com.znet.reconnaissance.model.Client;
import com.znet.reconnaissance.model.CommandMessage;


@Component
public class ServiceWebSocketHandler extends TextWebSocketHandlerAdapter {

	private Map<String, Client<?>> clients = 
		new ConcurrentHashMap<String, Client<?>>();
	
	@Inject
	public ServiceWebSocketHandler(Map<String, Handler<?, ?>> handlers) {
		initialize(handlers);
	}
	
	@Override
    public void afterConnectionEstablished(final WebSocketSession session) 
    		throws Exception {
		
		WSClient client = new WSClient(session);
		client.connected();
		
		this.clients.put(session.getId(), client);
    }

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage txtMessage)
    		throws Exception {
    	
    	// TODO: queue off into separate thread to allow websocket worker to work
		
    	// get client
		Client<?> client = this.getClient(session);
		if (client == null) {
			throw new IllegalStateException("no available client: " + session.getId());
		}
    	
    	// process
    	CommandMessage message = new CommandMessage(txtMessage.getPayload());
    	client.process(message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status)
    		throws Exception {
    	
    	// TODO: update health state
    	Client<?> client = this.getClient(session);
    	if (client != null) { this.disconnect(client); }
    }
    
    protected Client<?> getClient(WebSocketSession session) {
    	return this.clients.get(session.getId());
    }
    
    protected void disconnect(Client<?> client) {
    	this.clients.remove(client.getId());
    	client.disconnected();
    }
    
    private void initialize(Map<String, Handler<?, ?>> handlers) {
		for (Map.Entry<String, Handler<?, ?>> entry : handlers.entrySet()) {
			String name = entry.getKey();
			Handler<?, ?> handler = entry.getValue();
			HandlerFactory.register(name, handler);
		}
	}
}
