package com.znet.reconnaissance.handlers;

import com.znet.reconnaissance.commands.HeartbeatCommand.Heartbeat;
import com.znet.reconnaissance.model.Client;

public class HeartbeatHandler implements Handler<Heartbeat, Heartbeat> {

	@Override
	public Heartbeat process(Client<?> client, Heartbeat value) {
		return value;
	}

}
