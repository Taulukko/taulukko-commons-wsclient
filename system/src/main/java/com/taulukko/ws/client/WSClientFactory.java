package com.taulukko.ws.client;

import com.taulukko.ws.client.config.WSConfig;

public class WSClientFactory {

	private static WSConfig wsConfig;

	public static WSClient getClient(String server) {

		// loadProperties();
		server = server.trim();

		String url = wsConfig.getURL(server);
		String contextName = wsConfig.getContextName(server);

		if (url == null) {
			System.err.println(server + " not found. Check your ws-client.properties");
			return null;
		}

		url = url.trim();

		if (contextName == null) {
			contextName = "ROOT";
		}
		contextName = contextName.trim();

		String contextPart = "";

		if (!contextName.equals("ROOT")) {
			contextPart = "/" + contextName;
		}

		WSClient client = new WSClient(url + contextPart);

		return client;
	}

	public static void setConfig(WSConfig wsConfig) {

		WSClientFactory.wsConfig = wsConfig;
	}

	public static WSConfig getConfig() {

		return WSClientFactory.wsConfig;
	}

}
