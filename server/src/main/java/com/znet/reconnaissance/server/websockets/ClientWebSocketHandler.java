package com.znet.reconnaissance.server.websockets;

import java.util.Timer;
import java.util.TimerTask;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.adapter.TextWebSocketHandlerAdapter;

public class ClientWebSocketHandler extends TextWebSocketHandlerAdapter {

	@Override
    public void afterConnectionEstablished(final WebSocketSession session) 
    		throws Exception {
		
		System.out.println("WEB SOCKET OPEN: " + session);
		new Timer().scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				System.out.println("SEND MESSAGE: ");
				try { session.sendMessage(new TextMessage("TESTING")); }
				catch (Exception e) { e.printStackTrace(); }
			}
		}, 3000, 5000);
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
    	System.out.println("WEB SOCKET CLOSE: " + session + ": " + status);
    }
}
