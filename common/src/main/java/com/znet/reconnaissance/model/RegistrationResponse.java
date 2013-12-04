package com.znet.reconnaissance.model;



public class RegistrationResponse {
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