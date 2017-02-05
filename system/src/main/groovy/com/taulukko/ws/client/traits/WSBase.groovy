package com.taulukko.ws.client.traits;

import java.util.concurrent.atomic.AtomicBoolean

import com.taulukko.commons.util.io.EFile
import com.taulukko.ws.client.WSClientFactory
import com.taulukko.ws.client.WSConfig
import com.taulukko.ws.client.WSConfigBuilder

public abstract class WSBase {
	private static AtomicBoolean loaded = new AtomicBoolean(false);
	private static WSConfig wsconfig;

	public static WSConfig getConfig() {
		if(this.wsconfig == null) {
			synchronized (loaded) {
				if(loaded.get()) {
					return wsconfig;
				}

				WSConfigBuilder configBuilder =  new WSConfigBuilder(
						) ;



				WSConfig.startDefault(WSConfig.class,configBuilder,
						"ws-client", WSClientFactory.path);

				WSConfig config = WSConfig.<WSConfig>getInstance(WSConfig.class);


				wsconfig = config;

				loaded.set(false);
			}
		}
		return wsconfig;
	}
}
