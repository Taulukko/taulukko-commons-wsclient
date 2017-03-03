package com.taulukko.ws.client;

import java.util.Map;

import com.taulukko.commons.util.config.ConfigBase;
import com.taulukko.commons.util.config.Reloadable;

public class ReloadableConfig implements Reloadable {

	private WSConfig config;

	public WSConfig getConfig() {
		return config;
	}

	public void setConfig(WSConfig config) {
		this.config = config;
	}

	@Override
	public void reload(ConfigBase configbase, Map<String, String> properties) {

		configbase.setExtended(properties);
	}

}
