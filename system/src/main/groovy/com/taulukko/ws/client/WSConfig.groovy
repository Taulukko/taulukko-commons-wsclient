package com.taulukko.ws.client;

import org.apache.commons.lang3.StringUtils

import com.taulukko.commons.util.config.ConfigBase
import com.taulukko.commons.util.config.Reloadable
 
public class WSConfig extends ConfigBase {

	WSConfig(Reloadable reloadable) {
		super(reloadable, WSConfig.class);
	} 

 
	String getURL(String server) {
		String key = "${server}.url";
		return this[key];
	}

	String getContextName(String server) {
		return this.get(  "${server}.contextName".toString());
	}

	boolean endsWithSeparator() {
		return StringUtils.defaultIfEmpty(this.get("endsWithSeparator"),"false").toBoolean();
	}
}
