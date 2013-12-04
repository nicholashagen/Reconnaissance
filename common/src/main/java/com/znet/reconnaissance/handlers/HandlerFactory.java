package com.znet.reconnaissance.handlers;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class HandlerFactory {

	private static Map<String, HandlerMetadata> handlers = 
		new HashMap<String, HandlerMetadata>();
	
	private HandlerFactory() {
		super();
	}
	
	public static Handler<?, ?> getHandler(String command) {
		HandlerMetadata metadata = getHandlerMetadata(command);
		return (metadata == null ? null : metadata.getHandler());
	}
	
	public static HandlerMetadata getHandlerMetadata(String command) {
		return handlers.get(command);
	}
	
	public static void register(String command, Handler<?, ?> handler) {
		Class<?> targetType = null, responseType = null;
		Method[] methods = handler.getClass().getMethods();
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			if ("process".equals(method.getName()) &&
				method.getParameterTypes().length == 2 && 
				!method.isBridge()) {
				
				targetType = method.getParameterTypes()[1];
				responseType = method.getReturnType();
				break;
			}
		}
		
		if (targetType == null || responseType == null) {
			throw new IllegalStateException(
				"invalid command: " + command + ": " + handler.getClass()
			);
		}
		
		handlers.put(command, 
			new HandlerMetadata(command, handler, targetType, responseType)
		);
	}
	
	public static class HandlerMetadata {
		private String name;
		private Handler<?, ?> handler;
		private Class<?> targetType;
		private Class<?> responseType;
		
		public HandlerMetadata(String name, Handler<?, ?> handler,
				Class<?> targetType, Class<?> responseType) {
			this.name = name;
			this.handler = handler;
			this.targetType = targetType;
			this.responseType = responseType;
		}
		
		public String getName() { return this.name; }
		public Handler<?, ?> getHandler() { return this.handler; }
		public Class<?> getTargetType() { return this.targetType; }
		public Class<?> getResponseType() { return this.responseType; }
	}
}
