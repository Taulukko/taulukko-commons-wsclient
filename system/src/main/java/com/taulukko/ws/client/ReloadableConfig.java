package com.taulukko.ws.client;

import java.util.Properties;

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
	public void reload(ConfigBase configbase, Properties properties) {
		
		WSConfig config = (WSConfig) configbase;
		config.setProperties(properties);
	}

}
