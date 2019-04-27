package integration.com.taulukko.commons.ws.runner;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.taulukko.commons.util.io.EFile;
import com.taulukko.ws.client.Config;
import com.taulukko.ws.client.WSClient;
import com.taulukko.ws.client.WSClientException;
import com.taulukko.ws.client.WSClientFactory;

public class ConfigTest {

	@BeforeClass
	public static void init() throws Exception {
		WSClientFactory.start(EFile.getClassLoaderPath(), false);

	}

	@AfterClass
	public static void end() throws Exception {

	}

	@Test
	public void config() throws WSClientException, InterruptedException {

		WSClient client = WSClientFactory.getClient("util");

		Assert.assertNotNull(client);
		Assert.assertEquals("http://localhost:8181", Config.getLastConfig().getURL("util"));

	}

	@Test
	public void sanitize() throws WSClientException, InterruptedException {

		String uri = "file:///C:\\Users\\7095587\\.m2\\repository\\org\\apache";

		String expected = "file:///Users/7095587/.m2/repository/org/apache";
		Assert.assertEquals(expected, Config.sanitizeURI(uri));

	}

}
