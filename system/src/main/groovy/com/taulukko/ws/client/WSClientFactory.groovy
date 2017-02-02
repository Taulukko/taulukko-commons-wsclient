package com.taulukko.ws.client;


public class WSClientFactory {

	private static final String PROPERTIES_FILE = "ws-client.properties";
	private static Properties properties = null;
	private static WSConfig config = null;
	private static String path = new File(".").getAbsolutePath();

	public static void start(String path) throws Exception {
		WSClientFactory.path = path;

		if (config == null) {

			WSConfigBuilder configBuilder =  new WSConfigBuilder(
					) ;

			WSConfig.startDefault(WSConfig.class,configBuilder,
					"ws-client", WSClientFactory.path);

			config =  WSConfig
					.<WSConfig> getInstance(WSConfig.class);
		}
	}

	public static WSClient getClient(String server) {

		loadProperties();
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

	private static void loadProperties() {
		try {

			if (properties != null) {
				return;
			}

			InputStream inputStream = WSClientFactory.class.getResourceAsStream "/$PROPERTIES_FILE";

			if (inputStream == null) {
				String realPath = Thread.currentThread()
						.getContextClassLoader().getResource("./").toString()
						.replace("file:/", "");

				String soname = System.getProperty("os.name").toLowerCase();

				boolean notStartSeparator = (realPath.charAt(0) != '\'' && realPath
						.charAt(0) != '/');

				if (!soname.contains("windows") && notStartSeparator) {
					realPath = "/" + realPath;
				}

				inputStream = new FileInputStream(realPath + PROPERTIES_FILE);
			}
			properties = new Properties();
			properties.load(inputStream);
			inputStream.close();
		} catch (IOException e) {
			System.err.println(  "$PROPERTIES_FILE not found");
			e.printStackTrace();
		}
	}
}
