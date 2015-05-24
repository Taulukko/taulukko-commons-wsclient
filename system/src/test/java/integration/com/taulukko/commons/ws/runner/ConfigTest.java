package integration.com.taulukko.commons.ws.runner;

import java.io.File;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.taulukko.ws.client.Config;
import com.taulukko.ws.client.RESTClientFactory;
import com.taulukko.ws.client.WSClient;
import com.taulukko.ws.client.WSClientException;

public class ConfigTest {

	@BeforeClass
	public static void init() throws Exception {
		RESTClientFactory.start(new File(".").getAbsolutePath()
				+ "/src/main/resources/webapps/WEB-INF/classes", false);

	}

	@AfterClass
	public static void end() throws Exception {

	}

	@Test
	public void config() throws WSClientException, InterruptedException {

		WSClient client = RESTClientFactory.getClient("util");

		Assert.assertNotNull(client);
		Assert.assertEquals("http://localhost:8181", Config
				.<Config> getInstance().getURL("util"));

	}

}
