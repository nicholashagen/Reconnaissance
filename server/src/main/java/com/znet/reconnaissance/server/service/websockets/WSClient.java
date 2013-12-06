package com.znet.reconnaissance.server.service.websockets;

import java.io.IOException;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.znet.reconnaissance.model.Client;

public class WSClient extends Client<WebSocketSession> {

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