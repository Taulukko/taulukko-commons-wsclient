package com.taulukko.ws.client;

import java.io.File;

public class WSClientFactory {
 
	private static String path = new File(".").getAbsolutePath();

	public static void start(String path, final boolean j2ee) throws Exception {
		WSClientFactory.path = path;

		if (Config.getLastConfig() == null) {

			Config.startDefault("ws-client", WSClientFactory.path);
		}
	}

	public static WSClient getClient(String server) {

		// loadProperties();
		server = server.trim();

		String url = Config.getLastConfig().getURL(server);
		String contextName = Config.getLastConfig().getContextName(server);

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
 
}
