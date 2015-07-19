package com.taulukko.ws.client;

import com.taulukko.commons.util.config.ConfigBuilder;

public class WSConfigBuilder implements ConfigBuilder<Config> {

	private boolean j2ee = false;

	public WSConfigBuilder(boolean j2ee) {
		this.j2ee = j2ee;
	}

	@Override
	public Config createNewConfig() {
		ReloadableConfig reloadale = new ReloadableConfig();
		return new Config(reloadale, j2ee);
	}
}
