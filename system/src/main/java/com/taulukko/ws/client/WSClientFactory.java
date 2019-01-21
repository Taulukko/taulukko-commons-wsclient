package com.taulukko.ws.client;

import org.apache.commons.lang3.NotImplementedException;

import com.taulukko.ws.client.config.CLIENT_IMPLEMENTATION;

public class WSClientFactory extends WSBase {

	private static com.taulukko.ws.client.config.WSConfig config = null;

	/* Use setConfig */
	@Deprecated
	public static void start(String path) throws Exception {

	}

	public static com.taulukko.ws.client.config.WSConfig getConfig() throws Exception {
		return WSClientFactory.config;
	}

	public static void setConfig(com.taulukko.ws.client.config.WSConfig config) throws Exception {
		System.out.println("WSClient : New config " + config.getVersion() + " version loaded!" );
		WSClientFactory.config = config;
	}

	public static WSClient getClient(String server) throws WSClientException {

		server = server.trim();

		String url = config.getURL(server);
		String contextName = config.getContextName(server);

		if (url == null) {
			System.err.println("Not found configuration to server [" + server + "]");
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

		if (config.getClientImplementation() == CLIENT_IMPLEMENTATION.HTTPURLCONNECTION) {

			return new WSClientHttpURLConnection(url + contextPart, config.getHasTerminator(server));
		} else {
			throw new WSClientException(new NotImplementedException("TYPE CLIENT NOT FOUND"));
		}

	}

}
