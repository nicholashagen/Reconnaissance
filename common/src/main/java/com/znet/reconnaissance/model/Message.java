package com.znet.reconnaissance.model;

import com.znet.reconnaissance.commands.Command;

public class Message {

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
