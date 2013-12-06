package com.znet.reconnaissance.model;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.znet.reconnaissance.commands.Command;
import com.znet.reconnaissance.commands.HeartbeatCommand;
import com.znet.reconnaissance.commands.ResponseCommand;
import com.znet.reconnaissance.handlers.Handler;
import com.znet.reconnaissance.handlers.HandlerFactory;
import com.znet.reconnaissance.handlers.HandlerFactory.HandlerMetadata;
import com.znet.reconnaissance.util.Json;

public abstract class Client<T> {
	
	private static Random RANDOM = new Random();
	
	private String id;
	private T session;
	private boolean connected;
	private Set<ClientListener> listeners = new HashSet<>();
	private Map<String, Message> messages = new ConcurrentHashMap<>();
	
	private HeartbeatCallback heartbeatCallback = new HeartbeatCallback();
	
	public Client(String id) {
		this.id = id;
	}
	
	public Client(String id, T session) {
		this.id = id;
		this.session = session;
	}
	
	public String getId() {
		return this.id;
	}
	
	public T getSession() {
		return this.session;
	}
	
	protected void setSession(T session) {
		this.session = session;
	}
	
	public synchronized void addListener(ClientListener listener) {
		this.listeners.add(listener);
	}
	
	public synchronized void removeListener(ClientListener listener) {
		this.listeners.remove(listener);
	}
	
	public synchronized void connected() {
		this.connected = true;
		for (ClientListener listener : this.listeners) {
			listener.connected(this);
		}
	}
	
	public synchronized void registered() {
		for (ClientListener listener : this.listeners) {
			listener.registered(this);
		}
	}
	
	public synchronized void disconnected() {
		this.connected = false;
		for (ClientListener listener : this.listeners) {
			listener.disconnected(this);
		}
	}
	
	public void disconnect() {
		disconnected();
	}
	
	public boolean isConnected() { 
		return this.connected;
	}
	
	public void send(Command<?> command) {
		send(command, null);
	}
	
	public void send(Command<?> command, Callback callback) {
		send(nextId(), command, callback);
	}
	
	protected void send(String id, Command<?> command, Callback callback) {
		if (callback != null) {
			this.messages.put(id, new Message(id, this, command, callback));
		}
		
		CommandMessage message = new CommandMessage(id, command);
		System.out.println("S: " + message.getPayload());
		try { sendMessage(message.getPayload()); }
		catch (Exception exception) {
			this.messages.remove(id);
			throw new IllegalArgumentException(exception);
		}
	}
	
	protected abstract void sendMessage(String payload);
	
	public boolean heartbeat() {
		if (!heartbeatCallback.isReceived()) { 
			return false;
		}
		
		this.heartbeatCallback.update();
		this.send(new HeartbeatCommand(), heartbeatCallback);
		return true;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void process(CommandMessage message)
    		throws Exception {
    	
		System.out.println("R: " + message.getPayload());
		
		// process responses
		if (ResponseCommand.NAME.equals(message.getName())) {
			processResponse(message);
			return;
		}
		
		// process custom handlers
		HandlerMetadata metadata =
			HandlerFactory.getHandlerMetadata(message.getName());
		if (metadata == null) {
			throw new IllegalArgumentException("invalid command: " + message);
		}

		// parse json
		Class<?> targetType = metadata.getTargetType();
		Object value = Json.read(message.getBody(), targetType);
		
		// process command
		Handler handler = metadata.getHandler();
		Object result = handler.process(this, value);

		// send response
		if (result != null) {
			this.send(message.getId(), new ResponseCommand(result), null);
		}
    }
	
	protected void processResponse(CommandMessage message) {
		String id = message.getId();
		Message msg = this.messages.remove(id);
		if (msg != null) {
			Command<?> command = msg.getCommand();
			Object value = Json.read(message.getBody(), command.getTargetType());
			msg.getCallback().process(new Response(id, this, command, value));
		}
	}
	
	protected static String nextId() {
		final int start = 268435456; // 0 x 10 00 00 00
		final int range = Integer.MAX_VALUE - start;
		return Integer.toHexString(start + RANDOM.nextInt(range));
	}

	protected class HeartbeatCallback implements Callback {

		public long lastHeartbeatSent = -1;
		public long lastHeartbeatReceived = -1;
		
		public void update() {
			this.lastHeartbeatSent = System.currentTimeMillis();
			this.lastHeartbeatReceived = -1;
		}
		
		@Override
		public void process(Response response) {
			this.lastHeartbeatReceived = System.currentTimeMillis();
		}

		public boolean isReceived() {
			return this.lastHeartbeatSent < 0 ||
				this.lastHeartbeatReceived > this.lastHeartbeatSent;
		}
	}
	
	public static class Message {

		private String id;
		private Client<?> client;
		private Command<?> command;
		private Callback callback;
		
		public Message(String id, Client<?> client, Command<?> command,
				Callback callback) {
			this.id = id;
			this.client = client;
			this.command = command;
			this.callback = callback;
		}
		
		public String getId() { return this.id; }
		public Client<?> getClient() { return this.client; }
		public Command<?> getCommand() { return this.command; }
		public Callback getCallback() { return this.callback; }
	}
}
