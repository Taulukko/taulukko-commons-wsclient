package com.taulukko.ws.client;

import com.taulukko.commons.util.config.Reloadable;


/*
 * Use new package  : com.taulukko.ws.client.config.WSConfig
 * */
@Deprecated
public class WSConfig  extends com.taulukko.ws.client.config.WSConfig{

	/*
	 * Use WSConfigBuilder
	 * */
	@Deprecated
	public WSConfig(Reloadable reloadable) {
		super(reloadable); 
	}
 
}
