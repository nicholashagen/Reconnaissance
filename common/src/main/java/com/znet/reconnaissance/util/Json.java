package com.znet.reconnaissance.util;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.znet.reconnaissance.util.BeanUtil.BeanException;



public class Json {

	private Json() {
		super();
	}
	
	public static <T> T read(String value, Class<T> targetType) {
		try {
			JsonReader reader = new JsonReader(value);
			return reader.read(targetType);
		}
		catch (Exception exception) {
			throw new IllegalStateException(exception);
		}
	}
	
	public static String write(Object value) {
		StringWriter writer = new StringWriter(8192);
		try (JsonWriter jsonWriter = new JsonWriter(writer)) { 
			jsonWriter.write(value);
		}
		catch (IOException ioe) { 
			throw new IllegalStateException(ioe); 
		}
		
		return writer.getBuffer().toString();
	}
	
	public static class JsonReader {
		private JSONObject json;
		
		public JsonReader(String json) throws IOException {
			try { this.json = new JSONObject(new JSONTokener(json)); }
			catch (Exception exception) { throw new IOException(exception); }
		}
		
		public <T> T read(Class<T> type) 
				throws IOException, JSONException {
			return readValue(this.json, type, type);
		}
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		protected <T> T readValue(Object json, Class<T> type, Type genericType)
				throws IOException, JSONException {
			
			if (json == null) {
				return null;
			}
			else if (CharSequence.class.isAssignableFrom(type)) {
				return (T) json.toString();
			}
			else if (int.class.isAssignableFrom(type)) {
				return (T) json;
			}
			else if (long.class.isAssignableFrom(type)) {
				return (T) json;
			}
			else if (double.class.isAssignableFrom(type)) {
				return (T) json;
			}
			else if (Number.class.isAssignableFrom(type)) {
				return (T) json;
			}
			else if (boolean.class.isAssignableFrom(type)) {
				return (T) json;
			}
			else if (Boolean.class.isAssignableFrom(type)) {
				return (T) json;
			}
			else if (type.isArray()) {
				return (T) readArray((JSONArray) json, type.getComponentType());
			}
			else if (Collection.class.isAssignableFrom(type)) {
				if (!(genericType instanceof ParameterizedType)) {
					throw new IOException("unknown type of collection");
				}

				Class<?> ptype = Generics.getIterationType(genericType);
				if (Object.class.equals(ptype)) {
					throw new IOException("unable to resolve generic collection");
				}
				
				Class ctype = type;
				return (T) readCollection((JSONArray) json, ctype, ptype);
			}
			else if (Map.class.isAssignableFrom(type)) {
				if (!(genericType instanceof ParameterizedType)) {
					throw new IOException("unknown type of collection");
				}

				Class<?> ktype = Generics.getKeyType(genericType);
				if (!String.class.equals(ktype)) {
					throw new IOException("unable to resolve generic map key");
				}
				
				Class<?> vtype = Generics.getValueType(genericType);
				if (Object.class.equals(vtype)) {
					throw new IOException("unable to resolve generic map value");
				}
				
				Class ctype = type;
				return (T) readMap((JSONObject) json, ctype, vtype);
			}
			else {
				return readObject((JSONObject) json, type);
			}
		}
		
		@SuppressWarnings("unchecked")
		protected <T> T readObject(JSONObject json, Class<T> type) 
				throws BeanException, IOException, JSONException {
			
			T result = BeanUtil.newInstance(type);
			
			Iterator<String> it = json.keys();
			Map<String, PropertyDescriptor> properties = 
				BeanUtil.getProperties(type);
			
			while (it.hasNext()) {
				String key = it.next();
				PropertyDescriptor property = properties.get(key);
				if (property == null) { continue; }
				
				// TODO: if collection, read property and clear/re-use
				
				BeanUtil.setProperty(
					result, property,
					readValue(json.get(key), property.getPropertyType(),
							  BeanUtil.getGenericType(property))
				);
			}
	
			return result;
		}
		
		@SuppressWarnings("unchecked")
		protected <V> Map<String, V> readMap(JSONObject json, 
			Class<? extends Map<String, V>> type, Class<V> vtype) 
				throws BeanException, IOException, JSONException {
			
			Map<String, V> result = createMap(type);
			
			Iterator<String> it = json.keys();
			while (it.hasNext()) {
				String key = it.next();
				result.put(key, readValue(json.get(key), vtype, vtype));
			}
	
			return result;
		}
		
		@SuppressWarnings("unchecked")
		protected <T> T[] readArray(JSONArray array, Class<T> type)
				throws IOException, JSONException {
			
			int length = array.length();
			T[] result = (T[]) Array.newInstance(type, length);
			for (int i = 0; i < length; i++) {
				Array.set(result, i, readValue(array.get(i), type, type));
			}
			
			return result;
		}
		
		protected <T> Collection<T> 
		readCollection(JSONArray array, 
			Class<? extends Collection<T>> type, Class<T> ptype)
				throws IOException, JSONException {
			
			int length = array.length();
			Collection<T> result = createCollection(type, length);
			for (int i = 0; i < length; i++) {
				result.add(readValue(array.get(i), ptype, ptype));
			}
			
			return result;
		}
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		protected <K, V> Map<K, V> createMap(Class<? extends Map<K, V>> type) {
			
			// check if type has a default constructor
			Constructor<?>[] ctors = type.getConstructors();
			for (int i = 0; i < ctors.length; i++) {
				if (ctors[i].getParameterTypes().length == 0) {
					try { return (Map) ctors[i].newInstance(); }
					catch (Exception exception) {
						// ignore and fall through below
					}
				}
			}
			
			// check for common types
			if (type.isAssignableFrom(SortedMap.class)){
				return new TreeMap();
			}
			else if (type.isAssignableFrom(Map.class)) {
				return new HashMap();
			}
			
			throw new IllegalStateException(
				"no available map implementation: " + type
			);
		}
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		protected <T> Collection<T> 
		createCollection(Class<? extends Collection<T>> type, int size) {
			
			// check if type has a default constructor
			Constructor<?>[] ctors = type.getConstructors();
			for (int i = 0; i < ctors.length; i++) {
				if (ctors[i].getParameterTypes().length == 0) {
					try { return (Collection) ctors[i].newInstance(); }
					catch (Exception exception) {
						// ignore and fall through below
					}
				}
			}
			
			// check for common types
			if (type.isAssignableFrom(Collection.class) || 
				type.isAssignableFrom(List.class)) {
				return new ArrayList(size);
			}
			
			throw new IllegalStateException(
				"no available collection implementation: " + type
			);
		}
	}
	
	public static class JsonWriter implements Closeable {
		
		private Writer writer;
		
		public JsonWriter(Writer writer) {
			this.writer = new BufferedWriter(writer);
		}
		
		public void write(Object value) throws IOException {
			this.writeValue(value);
		}
		
		@Override
		public void close() throws IOException {
			this.writer.close();
		}

		@SuppressWarnings("rawtypes")
		protected void writeValue(Object value) throws IOException {
			if (value == null) {
				writeNull();
			}
			else if (value instanceof CharSequence) {
				writeString(value.toString());
			}
			else if (value instanceof Number) {
				writeNumber((Number) value);
			}
			else if (value instanceof Boolean) {
				writeBoolean((Boolean) value);
			}
			else if (value.getClass().isArray()) {
				writeArray(value);
			}
			else if (value instanceof Collection) {
				writeCollection((Collection) value);
			}
			else if (value instanceof Map) {
				writeMap((Map) value);
			}
			else {
				writeObject(value);
			}
		}
		
		protected void writeNull() throws IOException {
			this.writer.write("null");
		}
		
		protected void writeString(String value) throws IOException {
			this.writer.write('"');
			this.writer.write(value.replace("\"", "\\\""));
			this.writer.write('"');
		}
		
		protected void writeNumber(Number value) throws IOException {
			this.writer.write(value.toString());
		}
		
		protected void writeBoolean(Boolean value) throws IOException {
			this.writer.write(value.toString());
		}
		
		protected void writeArray(Object value) throws IOException {
			int length = Array.getLength(value);
			this.writer.write('[');
			for (int i = 0; i < length; i++) {
				if (i > 0) { this.writer.write(','); }
				this.writeValue(Array.get(value, i));
			}
			this.writer.write(']');
		}
		
		protected void writeCollection(Collection<?> value) throws IOException {
			boolean first = true;
			this.writer.write('[');
			for (Object item : value) {
				if (!first) { this.writer.write(','); }
				this.writeValue(item);
				if (first) { first = false; }
			}
			this.writer.write(']');
		}
		
		protected void writeMap(Map<?, ?> value) throws IOException {
			boolean first = true;
			this.writer.write('{');
			for (Map.Entry<?, ?> entry : value.entrySet()) {
				Object key = entry.getKey(), item = entry.getValue();
				if (key == null) { continue; }
				
				if (!first) { this.writer.write(','); }
				this.writeString(key.toString());
				this.writer.write(':');
				this.writeValue(item);
				if (first) { first = false; }
			}
			this.writer.write('}');
		}
		
		protected void writeObject(Object value) 
				throws BeanException, IOException {
			boolean first = true;
			this.writer.write('{');
			BeanInfo bean = BeanUtil.getBeanInfo(value);
			PropertyDescriptor[] properties = bean.getPropertyDescriptors();
			for (int i = 0; i < properties.length; i++) {
				PropertyDescriptor property = properties[i];
				if (!BeanUtil.isSupported(property.getPropertyType())) {
					continue;
				}
				
				if (!first) { this.writer.write(','); }
				this.writeString(property.getName());
				this.writer.write(':');
				this.writeValue(BeanUtil.getProperty(value, property));
				if (first) { first = false; }
			}
			this.writer.write("}");
		}
	}
}
