package com.znet.reconnaissance.model;

import com.znet.reconnaissance.commands.Command;

public class Response {

	private String id;
	private Client<?> client;
	private Command<?> command;
	private Object result;
	
	public Response(String id, Client<?> client, Command<?> command, 
			Object result) {
		this.id = id;
		this.client = client;
		this.command = command;
		this.result = result;
	}
	
	public String getId() { return this.id; }
	public Client<?> getClient() { return this.client; }
	public Command<?> getCommand() { return this.command; }
	public Object getResult() { return this.result; }
}
