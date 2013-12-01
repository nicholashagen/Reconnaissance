package com.znet.reconnaissance.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Reconnaissance {

	private String serverUrl;
	
	public Reconnaissance(String serverUrl) {
		this.serverUrl = serverUrl;
	}
	
	public static void main(String[] args) throws Exception {
		Registration reg = new Registration();
		reg.setTree("group:subgroup:name");
		reg.setName("name");
		reg.setType("web");
		reg.setEnvironment("dev");
		reg.setProfiles(Arrays.asList("test", "hsqldb"));
		reg.addMetadata("test", "data");
		
		new Reconnaissance("http://localhost:8080").register(reg);
	}
	
	public void register(Registration registration) throws IOException {
		
		// setup POST registration request to server
		URL url = new URL(String.format("%s/register", this.serverUrl));
		URLConnection connection = url.openConnection();
		connection.setDoOutput(true);
		connection.setRequestProperty("Accept", "application/json");
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("User-Agent", "Simple Java Client");
		
		// output the JSON registration as the payload
		try (Writer writer = new BufferedWriter(
				new OutputStreamWriter(connection.getOutputStream()))) {
			
			registration.toJSON().write(writer);
		}
		catch (JSONException exception) {
			throw new IOException("invalid json", exception);
		}
		
		// read in the JSON registration response
		RegistrationResponse response = null;
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(connection.getInputStream()
		))) {
			
			JSONObject json = new JSONObject(new JSONTokener(reader));
			response = RegistrationResponse.fromJSON(json);
		}
		catch (JSONException exception) {
			throw new IOException("invalid json", exception);
		}
		
		// process response
		System.out.println("COMPLETED: " + response.getTimestamp() + ", " +
				response.getRegistration().getName());
	}
	
	public static class RegistrationResponse {
		
		private boolean successful;
		private long timestamp;
		private Registration registration;
		
		public RegistrationResponse() {
			super();
		}

		public boolean isSuccessful() {
			return this.successful;
		}

		public void setSuccessful(boolean successful) {
			this.successful = successful;
		}

		public long getTimestamp() {
			return this.timestamp;
		}

		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}

		public Registration getRegistration() {
			return this.registration;
		}

		public void setRegistration(Registration registration) {
			this.registration = registration;
		}
		
		public static RegistrationResponse fromJSON(JSONObject json) 
			throws JSONException {
			
			RegistrationResponse response = new RegistrationResponse();
			response.setSuccessful(json.getBoolean("successful"));
			response.setTimestamp(json.getLong("timestamp"));
			response.setRegistration(
				Registration.fromJSON(json.getJSONObject("registration"))
			);
			
			return response;
		}
	}
	
	public static class Registration {
		private String tree;
		private String name;
		private String type;
		private String environment;
		private List<String> profiles = new ArrayList<String>();
		private Map<String, Object> metadata = new HashMap<String, Object>();
		
		public Registration() {
			super();
		}

		public String getTree() {
			return this.tree;
		}

		public void setTree(String tree) {
			this.tree = tree;
		}

		public String getName() {
			return this.name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getType() {
			return this.type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public List<String> getProfiles() {
			return this.profiles;
		}

		public void setProfiles(List<String> profiles) {
			this.profiles = profiles;
		}

		public void addProfile(String profile) {
			this.profiles.add(profile);
		}
		
		public String getEnvironment() {
			return this.environment;
		}

		public void setEnvironment(String environment) {
			this.environment = environment;
		}

		public Map<String, Object> getMetadata() {
			return this.metadata;
		}

		public void setMetadata(Map<String, Object> metadata) {
			this.metadata = metadata;
		}
		
		public void addMetadata(String key, Object value) {
			this.metadata.put(key, value);
		}
		
		public JSONObject toJSON() {
			try {
				JSONObject json = new JSONObject();
				json.put("tree", this.getTree());
				json.put("name", this.getName());
				json.put("type", this.getType());
				json.put("environment", this.getEnvironment());
				json.put("profiles", new JSONArray(this.getProfiles()));
				json.put("metadata", new JSONObject(this.getMetadata()));
				return json;
			}
			catch (JSONException exception) {
				throw new IllegalStateException("invalid JSON: " + exception);
			}
		}
		
		public static Registration fromJSON(JSONObject json) 
			throws JSONException {
			
			Registration registration = new Registration();
			registration.setTree(json.getString("tree"));
			registration.setName(json.getString("name"));
			registration.setType(json.getString("type"));
			registration.setEnvironment(json.getString("environment"));
			
			JSONArray profiles = json.getJSONArray("profiles");
			for (int i = 0; i < profiles.length(); i++) {
				registration.addProfile(profiles.getString(i));
			}
			
			JSONObject metadata = json.getJSONObject("metadata");
			Iterator<?> iterator = metadata.keys();
			while (iterator.hasNext()) {
				String name = (String) iterator.next();
				registration.addMetadata(name, metadata.get(name));
			}
			
			return registration;
		}
	}
}
