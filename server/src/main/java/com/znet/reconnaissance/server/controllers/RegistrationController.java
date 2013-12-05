package com.znet.reconnaissance.server.controllers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collection;

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

import com.znet.reconnaissance.model.Registration;
import com.znet.reconnaissance.model.RegistrationResponse;
import com.znet.reconnaissance.server.service.Service;
import com.znet.reconnaissance.server.service.ServiceRegistry;

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

			if (input == null) { return; }
			
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
	public Collection<Service> list(HttpServletResponse response) {
		response.setHeader("max-age", "10000");
		return serviceRegistry.listServices();
    }
	
	@ResponseBody
	@RequestMapping(value="/register2", method=RequestMethod.POST)
	public RegistrationResponse register(@RequestBody Registration registration) {
		serviceRegistry.registerService(registration);
        return new RegistrationResponse(registration);
    }
}
