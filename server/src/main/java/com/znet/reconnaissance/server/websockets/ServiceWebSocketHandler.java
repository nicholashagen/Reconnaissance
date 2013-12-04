package com.znet.reconnaissance.server.websockets;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
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
		startMonitors();
	}
	
	@Override
    public void afterConnectionEstablished(final WebSocketSession session) 
    		throws Exception {
		
		this.clients.put(session.getId(), new WSClient(session));
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
    	// disconnect client
    	this.clients.remove(client.getId());
    }
    
    private void startMonitors() {
    	// TODO: spread these out so we don't push all at once
    	//       set timer to more like 5s or less and walk clients and check
    	//       if last sent has elapsed 30s and then check
		final int interval = 30000; // 30s
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				final Set<String> clientIds = clients.keySet();
				for (String clientId : clientIds) {
					Client<?> client = clients.get(clientId);
					if (client != null) { 
						if (!client.heartbeat()) {
							disconnect(client);
						}
					}
				}
			}
		}, interval, interval);
    }
    
    private void initialize(Map<String, Handler<?, ?>> handlers) {
		for (Map.Entry<String, Handler<?, ?>> entry : handlers.entrySet()) {
			String name = entry.getKey();
			Handler<?, ?> handler = entry.getValue();
			HandlerFactory.register(name, handler);
		}
	}

    protected static class WSClient extends Client<WebSocketSession> {

		public WSClient(WebSocketSession session) {
			super(session.getId(), session);
		}

		@Override
		protected void sendMessage(String payload) {
			try { this.getSession().sendMessage(new TextMessage(payload)); }
			catch (IOException ioe) { 
				throw new IllegalStateException(ioe);
			}
		}
    	
    }
}
