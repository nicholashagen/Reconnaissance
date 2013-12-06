package com.znet.reconnaissance.server.notifications;

public class Notification {

	private String type;
	private String target;
	private String message;
	
	public Notification() {
		super();
	}
	
	public Notification(String type, String message) {
		this(type, null, message);
	}
	
	public Notification(String type, String target, String message) {
		this.type = type;
		this.target = target;
		this.message = message;
	}
	
	public String getType() { return this.type; }
	public void setType(String type) { this.type = type; }
	
	public String getTarget() { return this.target; }
	public void setTarget(String target) { this.target = target; }
	
	public String getMessage() { return this.message; }
	public void setMessage(String message) { this.message = message; }
}
