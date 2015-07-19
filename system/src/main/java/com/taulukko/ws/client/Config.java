package com.taulukko.ws.client;

import java.util.Properties;

import com.taulukko.commons.util.config.ConfigBase;
import com.taulukko.commons.util.config.Reloadable;

public class Config extends ConfigBase {

	private Properties properties = null;
	
	public Config(Reloadable reloadable, boolean j2ee) {
		super(reloadable, j2ee,Config.class);
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	
	public String getURL(String server)
	{
		return properties.getProperty(server + ".url");		
	}
	
	public String getContextName(String server)
	{
		return properties.getProperty(server + ".contextName");
	}
}
