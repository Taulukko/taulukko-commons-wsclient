package com.taulukko.ws.client;

import com.taulukko.commons.util.config.ConfigBase
import com.taulukko.commons.util.config.Reloadable

public class WSConfig extends ConfigBase {

	private Properties properties = null;

	WSConfig(Reloadable reloadable) {
		super(reloadable, WSConfig.class);
	}

	Properties getProperties() {
		return properties;
	}

	void setProperties(Properties properties) {
		this.properties = properties;
	}

	String getURL(String server) {
		properties.getProperty(   "${server}.url");
	}

	String getContextName(String server) {
		return properties.getProperty(  "${server}.contextName");
	}

	boolean endsWithSeparator() {
		return properties.getProperty("endsWithSeparator").toBoolean();
	}
}
