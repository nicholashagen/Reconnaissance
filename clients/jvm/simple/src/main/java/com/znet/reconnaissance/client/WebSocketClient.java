package com.znet.reconnaissance.client;

import java.net.URI;

import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import com.znet.reconnaissance.client.WebSocketClient.WebSocketSession;
import com.znet.reconnaissance.model.Client;
import com.znet.reconnaissance.model.CommandMessage;

public class WebSocketClient extends Client<WebSocketSession> {

	private URI serverUri;
	
	public WebSocketClient(URI serverUri) {
		super(serverUri.toString());
		this.serverUri = serverUri;
	}

	public boolean connect() {
		WebSocketSession session = new WebSocketSession(serverUri, this);
		this.setSession(session);
		
		try { return session.connectBlocking(); }
		catch (InterruptedException ie) {
			throw new IllegalStateException(ie);
		}
	}
	
	@Override
	protected void sendMessage(String payload) {
		this.getSession().send(payload);
	}
	
	public static class WebSocketSession extends 
		org.java_websocket.client.WebSocketClient {
		
		private WebSocketClient client;
		
		public WebSocketSession(URI serverUri, WebSocketClient client) {
			super(serverUri, new Draft_17());
			this.client = client;
		}

		@Override
		public void onOpen(ServerHandshake handshakedata) {
			// nothing to do
		}

		@Override
		public void onMessage(String txtMessage) {
			try { this.client.process(new CommandMessage(txtMessage)); }
			catch (Exception exception) {
				throw new IllegalStateException(exception);
			}
		}

		@Override
		public void onError(Exception ex) {
			System.out.println("ERROR: " + ex.getMessage());
			ex.printStackTrace();
		}

		@Override
		public void onClose(int code, String reason, boolean remote) {
			System.out.println("CLOSEED " + code + ": " + reason);
		}
	}
}