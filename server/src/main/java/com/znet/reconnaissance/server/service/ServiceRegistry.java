package com.znet.reconnaissance.server.service;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import com.znet.reconnaissance.server.broadcast.BroadcastService;
import com.znet.reconnaissance.server.notifications.Notification;
import com.znet.reconnaissance.server.notifications.NotificationService;

@org.springframework.stereotype.Service
public class ServiceRegistry {

	@Inject private BroadcastService broadcastService;
	@Inject private NotificationService notificationService;
	
	private Map<String, Service> services = new ConcurrentHashMap<>();
	private ServiceListener serviceListener = new ServiceListenerHandler();
	
	public ServiceRegistry() {
		super();
	}

	public Collection<Service> getServices() {
		return this.services.values();
	}
	
	public boolean registerService(Service service) {

		// save service
		this.services.put(service.getId(), service);
		
		// add listener to handler status updates to invoke notifications
		service.addListener(serviceListener);
		
		// mark service as registered
		service.registered();
		
		// broadcast service update
		// TODO: broadcastService.broadcast(new UpdateCommand());
		
		// success
		return true;
	}
	
	public class ServiceListenerHandler extends ServiceListenerAdapter {
		
		@Override
		public void connected(Service service) {
			notificationService.notify(new Notification(
				"connected", service.getId(), 
				service.getName() + " has connected on " + service.getHostname()
			));
		}
		
		@Override
		public void registered(Service service) {
			notificationService.notify(new Notification(
				"registered", service.getId(), 
				service.getName() + " has registered on " + service.getHostname()
			));
		}
		
		@Override
		public void disconnected(Service service) {
			notificationService.notify(new Notification(
				"disconnected", service.getId(), 
				service.getName() + " has disconnected on " + service.getHostname()
			));
		}
	}
}
