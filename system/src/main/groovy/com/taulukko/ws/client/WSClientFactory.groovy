package com.taulukko.ws.client;

import com.taulukko.ws.client.traits.WSBase

public class WSClientFactory extends WSBase{

	private static final String PROPERTIES_FILE = "ws-client.properties";
	private static Properties properties = null;
	 
	private static String path = new File(".").getAbsolutePath();

	public static void start(String path) throws Exception {
		WSClientFactory.path = path;

		if (config == null) {

	 

			config =  WSConfig
					.<WSConfig> getInstance(WSConfig.class);
		}
	}

	public static WSClient getClient(String server) { 

 
		server = server.trim();

		String url = config.getURL(server);
		String contextName = config.getContextName(server);

		if (url == null) {
			System.err.println  "$server not found. Check your ws-client.properties";
			return null;
		}

		url = url.trim();

		if (contextName == null) {
			contextName = "ROOT";
		}
		contextName = contextName.trim();

		String contextPart = "";

		if (!contextName.equals("ROOT")) {
			contextPart = "/$contextName" ;
		}

		WSClient client = new WSClient(url + contextPart);

		return client;
	}
 
}
