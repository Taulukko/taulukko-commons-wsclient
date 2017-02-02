package com.taulukko.ws.client;

import com.taulukko.commons.util.config.ConfigBuilder;

public class WSConfigBuilder implements ConfigBuilder<WSConfig> {
 
	 
	
	 

	public WSConfigBuilder() {
	}
	  


	@Override
	public WSConfig createNewConfig() {
		ReloadableConfig reloadale = new ReloadableConfig();
		return new WSConfig(reloadale);
	}
}
