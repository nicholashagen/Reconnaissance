package com.znet.reconnaissance.server.controllers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.znet.reconnaissance.server.ServiceRegistry;

@Controller
@RequestMapping(headers ={"Accept=application/json"})
public class RegistrationController {

	@Inject
	private ServiceRegistry serviceRegistry;

	public RegistrationController() {
		super();
	}
	
	@PostConstruct
	public void initialize() throws Exception {
		
		JSONObject json = null;
		
		try (InputStream input = 
				this.getClass().getResourceAsStream("/services.json")) {

			try (Reader reader = 
					new BufferedReader(new InputStreamReader(input))) {
				
				json = new JSONObject(new JSONTokener(reader));
			}
		}
		
		JSONArray services = json.getJSONArray("services");
		for (int i = 0; i < services.length(); i++) {
			
			JSONObject info = services.getJSONObject(i);
			Registration reg = new Registration();
			reg.setTree(info.getString("tree"));
			reg.setName(info.getString("name"));
			reg.setHostname(info.getString("host"));
			reg.setEnvironment(info.getString("environment"));
			reg.setType(info.getString("type"));
			this.serviceRegistry.registerService(reg);
		}
	}
	
	@ResponseBody
	@RequestMapping(value="/services", method=RequestMethod.GET)
	public Set<Registration> list(HttpServletResponse response) {
		response.setHeader("max-age", "10000");
		return serviceRegistry.listServices();
    }
	
	@ResponseBody
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public RegistrationResponse register(@RequestBody Registration registration) {
		serviceRegistry.registerService(registration);
        return new RegistrationResponse(registration);
    }
	
	public static class RegistrationResponse {
		private boolean successful;
		private long timestamp;
		private Registration registration;
		
		public RegistrationResponse(Registration registration) {
			this.successful = true;
			this.timestamp = System.currentTimeMillis();
			this.registration = registration;
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
	}
	
	public static class Registration {
		private String tree;
		private String name;
		private String type;
		private String hostname;
		private String environment;
		private List<String> profiles;
		private Map<String, String> metadata;
		
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

		public String getHostname() {
			return this.hostname;
		}
		
		public void setHostname(String hostname) {
			this.hostname = hostname;
		}
		
		public String getEnvironment() {
			return this.environment;
		}

		public void setEnvironment(String environment) {
			this.environment = environment;
		}

		public Map<String, String> getMetadata() {
			return this.metadata;
		}

		public void setMetadata(Map<String, String> metadata) {
			this.metadata = metadata;
		}
		
		// TODO: equals/hashCode based on tree,name,host,env,type,etc
	}
}
