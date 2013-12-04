package com.znet.reconnaissance.model;

import com.znet.reconnaissance.commands.Command;
import com.znet.reconnaissance.util.Json;

public class CommandMessage {

	private String id;
	private String name;
	private String body;
	private String payload;
	private Command<?> command;
	
	public CommandMessage(String payload) {
		String[] parts = payload.split(" ", 3);
		if (parts.length < 3) {
			throw new IllegalArgumentException("invalid message: " + payload);
		}
		
		this.id = parts[1];
		this.name = parts[0];
		this.body = parts[2];
		this.payload = payload;
	}
	
	public CommandMessage(String id, Command<?> command) {
		this.id = id;
		this.name = command.getName();
		this.command = command;
		this.body = Json.write(command.getTarget());
		this.payload = buildPayload(id, name, body);
	}

	public String getId() { 
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getBody() {
		return this.body;
	}
	
	public Command<?> getCommand() {
		return this.command; 
	}
	
	public String getPayload() {
		return this.payload;
	}
	
	@Override
	public String toString() {
		return "CommandMessage(" + this.name + ", " + this.id + ")";
	}
	
	protected static String buildPayload(String id, String name, String body) {
		StringBuilder payload = new StringBuilder(1024);
		payload.append(name).append(' ')
		       .append(id).append(' ')
		       .append(body);
		
		return payload.toString();
	}
}
