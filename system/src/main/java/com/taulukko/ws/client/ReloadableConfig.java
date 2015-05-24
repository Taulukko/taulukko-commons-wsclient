package com.taulukko.ws.client;

import java.util.Properties;

import com.taulukko.commons.util.config.ConfigBase;
import com.taulukko.commons.util.config.Reloadable;
 
public class ReloadableConfig implements Reloadable {

	private Config config;

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	@Override
	public void reload(ConfigBase configbase, Properties properties) {
		
		Config config = (Config) configbase;
		config.setProperties(properties);
	}

}
