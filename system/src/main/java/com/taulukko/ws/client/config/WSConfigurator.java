package com.taulukko.ws.client.config;

import java.util.HashMap;
import java.util.Map;

public abstract class WSConfigurator {

	protected Map<String, String> properties = new HashMap<>();
	protected CLIENT_IMPLEMENTATION clientImplementation = CLIENT_IMPLEMENTATION.HTTPURLCONNECTION;

	public Map<String, String> getProperties() {
		return properties;
	}

	public CLIENT_IMPLEMENTATION getClientImplementation() {
		return clientImplementation;
	}

}
