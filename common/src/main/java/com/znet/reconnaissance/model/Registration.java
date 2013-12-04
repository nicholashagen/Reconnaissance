package com.znet.reconnaissance.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Registration {
	private String tree;
	private String name;
	private String type;
	private String hostname;
	private String environment;
	private List<String> profiles = new ArrayList<>();
	private Map<String, String> metadata = new HashMap<>();
	
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
	
	public void addMetadata(String key, String value) {
		this.metadata.put(key, value);
	}
	
	// TODO: equals/hashCode based on tree,name,host,env,type,etc
}