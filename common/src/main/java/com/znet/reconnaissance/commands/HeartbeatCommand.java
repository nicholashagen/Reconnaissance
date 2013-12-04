package com.znet.reconnaissance.commands;

import com.znet.reconnaissance.commands.HeartbeatCommand.Heartbeat;

@Commandable(name=HeartbeatCommand.NAME)
public class HeartbeatCommand extends AnnotatedCommand<Heartbeat> {

	public static final String NAME = "heartbeat";
	
	public HeartbeatCommand() {
		super(new Heartbeat());
	}
	
	public static class Heartbeat {
		private long timestamp;
		
		public Heartbeat() {
			this.timestamp = System.currentTimeMillis();
		}
		
		public long getTimestamp() { return this.timestamp; }
		public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
	}
}
