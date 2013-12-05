package com.znet.reconnaissance.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class BeanUtil {
	
	private BeanUtil() {
		super();
	}
	
	public static <T> T newInstance(Class<T> type) 
			throws BeanException {
		try { return type.newInstance(); }
		catch (Exception exception) { throw new BeanException(exception); }
	}
	
	public static BeanInfo getBeanInfo(Object value) 
			throws BeanException {
		return (value == null ? null : getBeanInfo(value.getClass()));
	}
	
	public static BeanInfo getBeanInfo(Class<?> type) 
			throws BeanException {
		try { return Introspector.getBeanInfo(type); }
		catch (Exception exception) { throw new BeanException(exception); }
	}
	
	public static Map<String, PropertyDescriptor> getProperties(Object item) 
			throws BeanException {
		return (item == null ? null : getProperties(item.getClass()));
	}
	
	// TODO: cache these
	public static Map<String, PropertyDescriptor> getProperties(Class<?> type) 
			throws BeanException {
		BeanInfo bean = getBeanInfo(type);
		Map<String, PropertyDescriptor> properties = 
			new HashMap<String, PropertyDescriptor>();
		
		PropertyDescriptor[] descriptors = bean.getPropertyDescriptors();
		for (int i = 0; i < descriptors.length; i++) {
			if (isSupported(descriptors[i].getPropertyType())) {
				properties.put(descriptors[i].getName(), descriptors[i]);
			}
		}
		
		return properties;
	}
	
	public static Object getProperty(Object object, PropertyDescriptor property) 
			throws BeanException {
		try {
			Method readMethod = property.getReadMethod();
			return readMethod.invoke(object);
		}
		catch (Exception exception) {
			throw new BeanException(exception);
		}
	}
	
	public static void setProperty(Object object, PropertyDescriptor property, Object value) 
			throws BeanException {
		try {
			Method writeMethod = property.getWriteMethod();
			if (writeMethod == null) {
				// TODO: warn?
				return;
			}
			
			writeMethod.invoke(object, value);
		}
		catch (Exception exception) {
			throw new BeanException(exception);
		}
	}
	
	public static Type getGenericType(PropertyDescriptor property) {
		// try read method
		Method readMethod = property.getReadMethod();
		if (readMethod != null) {
			return readMethod.getGenericReturnType();
		}
		
		// try write method
		Method writeMethod = property.getWriteMethod();
		if (writeMethod != null) {
			return writeMethod.getGenericParameterTypes()[0];
		}
		
		// use default
		return property.getPropertyType();
	}
	
	public static boolean isSupported(Object value) {
		return value == null || isSupported(value.getClass());
	}
	
	public static boolean isSupported(Class<?> type) {
		if (Class.class.isAssignableFrom(type)) { return false; }
		else { return true; }
	}
	
	public static class BeanException extends RuntimeException {
		
		private static final long serialVersionUID = 1L;

		public BeanException(Exception cause) {
			super(cause);
		}
	}
}