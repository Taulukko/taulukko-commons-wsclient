package integration.com.taulukko.commons.ws.runner;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.taulukko.ws.client.WSClient;
import com.taulukko.ws.client.WSClientFactory;
import com.taulukko.ws.client.config.WSConfig;
import com.taulukko.ws.client.config.WSConfigBuilder;
import com.taulukko.ws.client.config.WSFileConfigurator;

public class ConfigTest {

	@BeforeClass
	public static void init() throws Exception {

	}

	@AfterClass
	public static void end() throws Exception {

	}

	@Test
	@Ignore
	public void configFile() throws Exception {

		WSConfig config = new WSConfigBuilder().configurator(new WSFileConfigurator("caminho do arquivo")).build();

		WSClient client = WSClientFactory.getClient("util");

		Assert.assertNotNull(client);
		Assert.assertEquals("http://localhost:8181", WSClientFactory.getConfig().getURL("util"));
		Assert.assertEquals("http://localhost:8181", config.getURL("util"));

	}

}
