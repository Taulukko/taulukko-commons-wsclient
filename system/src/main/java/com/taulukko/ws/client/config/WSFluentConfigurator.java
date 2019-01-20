package com.taulukko.ws.client.config;

public class WSFluentConfigurator extends WSConfigurator {

	public WSFluentConfigurator() {

	}

	public WSFluentConfigurator addServer(String servername, String url, String contextName) {
		return addServer(servername, url, contextName,false);
	}

	public WSFluentConfigurator addServer(String servername, String url, String contextName, boolean withTerminator) {
		this.properties.put(servername + ".url", url);
		this.properties.put(servername + ".contextName", contextName);
		this.properties.put(servername + ".hasTerminator", (withTerminator) ? "true" : "false");

		return this;
	}

}
