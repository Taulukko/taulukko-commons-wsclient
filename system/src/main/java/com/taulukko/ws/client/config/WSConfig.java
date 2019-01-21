package com.taulukko.ws.client.config;

import java.util.HashMap;
import java.util.Map;

import com.taulukko.commons.util.config.Reloadable;

public class WSConfig {
	


	private String version = "2.0.0";

	private CLIENT_IMPLEMENTATION clientImplementation = CLIENT_IMPLEMENTATION.HTTPURLCONNECTION;

	private Map<String, String> properties = new HashMap<>();

	/*
	 * Use WSConfigBuilder
	 */
	@Deprecated
	public WSConfig(Reloadable reloadable) {
		throw new RuntimeException("Deprecated: Not more userd");
	}

	WSConfig(WSConfigurator configurator) {
		if (configurator == null) {
			return;
		}
		this.properties = configurator.properties;
		this.clientImplementation = configurator.clientImplementation;
	}

	public CLIENT_IMPLEMENTATION getClientImplementation() {

		return clientImplementation;
	}

	public String getURL(String server) {
		String key = server + ".url";
		return properties.get(key);
	}

	public String getContextName(String server) {
		return properties.get(server + ".contextName");
	}

	public boolean getHasTerminator(String server) {
		return properties.get(server + ".hasTerminator").toLowerCase().equals("true");
	}
	
	public String getVersion() {
		return version;
	}
}
