package com.taulukko.ws.client.config;

import com.taulukko.ws.client.config.WSConfigurator;

public class WSConfigBuilder {

	private WSConfigurator configurator = null;

	public WSConfigBuilder() {
	}

	/*
	 * Use WSConfigBuilder
	 */
	@Deprecated
	public WSConfig createNewConfig() {
		throw new RuntimeException("Deprecated : Not more userd");
	}

	public WSConfigBuilder configurator(WSConfigurator configurator)
	{
		this.configurator = configurator;
		return this;
	}

	public WSConfig build() {
		return new WSConfig(configurator);
	}
}
