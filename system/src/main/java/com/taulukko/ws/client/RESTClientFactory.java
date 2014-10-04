package com.taulukko.ws.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class RESTClientFactory {

	private static final String PROPERTIES_FILE = "ws-client.properties";
	private static Properties properties = null;
	
	
	public static WSClient getClient(String server) {

		loadProperties();

		server = server.trim();

		String url = properties.getProperty(server + ".url");
		String contextName = properties.getProperty(server + ".contextName");

		if (url == null) {
			System.err.println(server
					+ " not found. Check your ws-client.properties");
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

	private static void loadProperties() {
		try {

			if (properties != null) {
				return;
			}

			InputStream in = RESTClientFactory.class.getResourceAsStream("/"
					+ PROPERTIES_FILE);
			if (in == null) {
				String realPath = Thread.currentThread()
						.getContextClassLoader().getResource("./").toString()
						.replace("file:/", "");

				String soname = System.getProperty("os.name").toLowerCase();

				boolean notStartSeparator = (realPath.charAt(0) != '\'' && realPath
						.charAt(0) != '/');

				if (!soname.contains("windows") && notStartSeparator) {
					realPath = "/" + realPath;
				}

				in = new FileInputStream(realPath + PROPERTIES_FILE);
			}
			properties = new Properties();
			properties.load(in);
			in.close();

		} catch (IOException e) {
			System.err.println(PROPERTIES_FILE + " not found");
			e.printStackTrace();
		}
	}
}
