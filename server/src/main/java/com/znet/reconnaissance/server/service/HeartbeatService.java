package com.znet.reconnaissance.server.service;

import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@org.springframework.stereotype.Service
public class HeartbeatService {
	
	@Inject private ServiceRegistry serviceRegistry;
	
	@PostConstruct
	public void initialize() {
		// TODO: spread these out so we don't push all at once
    	//       set timer to more like 5s or less and walk clients and check
    	//       if last sent has elapsed 30s and then check
		final int interval = 30000; // 30s
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new HeartbeatTask(), interval, interval);
	}
	
	public class HeartbeatTask extends TimerTask {
		
		@Override
		public void run() {
			for (Service service : serviceRegistry.getServices()) {
				if (service.isConnected() && !service.heartbeat()) {
					service.disconnect();
				}
			}
		}
	}
}
