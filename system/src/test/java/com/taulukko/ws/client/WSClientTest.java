package com.taulukko.ws.client;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.taulukko.ws.client.config.WSConfigBuilder;
import com.taulukko.ws.client.config.WSConfigurator;
import com.taulukko.ws.client.config.WSFluentConfigurator;

@RunWith(JUnit4.class)
public class WSClientTest extends WSBaseTest {

	private static Tomcat tomcat = null;
	private static Thread thread = null;

	@BeforeClass
	public static void init() throws Exception {

		thread = new Thread(new Runnable() {

			@Override
			public void run() {

				WSConfigurator configurator = new WSFluentConfigurator()
						.addServer("util-hasTerminator", "http://localhost:8181", "util", true)
						.addServer("util", "http://localhost:8181", "util", false)
						.addServer("get-hasTerminator", "https://httpbin.org", "get", true)
						.addServer("get", "https://httpbin.org", "get", false);

				com.taulukko.ws.client.config.WSConfig wsconfig = new WSConfigBuilder().configurator(configurator)
						.build();

				try {
					WSClientFactory.setConfig(wsconfig);
				} catch (Exception e1) {
					throw new RuntimeException(e1);
				}

				String webappDirLocation = "src/main/resources/webapps";

				tomcat = new Tomcat();

				// The port that we should run on can be set into an environment
				// variable
				// Look for that variable and default to 8080 if it isn't there.
				String webPort = System.getenv("PORT");
				if (webPort == null || webPort.isEmpty()) {
					webPort = "8181";
				}

				tomcat.setPort(Integer.valueOf(webPort));

				try {
					Thread.sleep(1000);
					tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());

					System.out
							.println("configuring app with basedir: " + new File(webappDirLocation).getAbsolutePath());

					tomcat.start();
					tomcat.getServer().await();

				} catch (ServletException | LifecycleException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		thread.start();

		while (tomcat == null) {
			Thread.sleep(100);
		}

		String state = tomcat.getServer().getStateName();

		while (!state.equals("STARTED")) {
			Thread.sleep(100);
			state = tomcat.getServer().getStateName();
		}

	}

	@AfterClass
	public static void end() throws Exception {
		tomcat.stop();
	}

	@Test
	public void execGetDirectQuery() throws WSClientException, InterruptedException {

		WSClient client = WSClientFactory.getClient("util");

		String path = "test/sum/";

		String output = client.execGet(path + "?number1=1&number2=2").getOutput();

		Assert.assertEquals("3", output);

	}

	@Test
	public void execGet() throws WSClientException, InterruptedException {

		WSClient client = WSClientFactory.getClient("util");

		String path = "test/sum/";
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("number1", 1);
		parameters.put("number2", 2);

		String output = client.execGet(path, parameters).getOutput();

		Assert.assertEquals("3", output);

	}

	@Test
	public void execGetWithoutTerminatorSuccess() throws WSClientException, InterruptedException {

		WSClient client = WSClientFactory.getClient("util");

		String path = "test/sum";
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("number1", 1);
		parameters.put("number2", 2);

		String output = client.execGet(path, parameters).getOutput();

		Assert.assertEquals("3", output);

	}

	@Test
	public void execGetMultiIn() throws WSClientException, InterruptedException {

		WSClient client = WSClientFactory.getClient("util");

		String path = "test/sum/";
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("number2", 2);

		String output = client.execGet(path + "?number1=1", parameters).getOutput();

		Assert.assertEquals("3", output);

	}

	@Test
	public void execPostWithoutTerminatorError() throws WSClientException, InterruptedException {

		WSClient client = WSClientFactory.getClient("util");

		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("number1", "1");
		parameters.put("number2", "2");

		String path = "test/sumPost";

		String response = client.execPost(path, parameters).getOutput();

		Assert.assertTrue(response.contains("Error"));

	}

	@Test
	public void execPostWithTerminator() throws WSClientException, InterruptedException {

		WSClient client = WSClientFactory.getClient("util-hasTerminator");

		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("number1", "1");
		parameters.put("number2", "2");

		String path = "test/sumPost/";

		String response = client.execPost(path, parameters).getOutput();

		Assert.assertEquals("3", response);

	}

	@Test
	public void execPostTerminatorRequiredSuccess() throws WSClientException, InterruptedException {

		try {

			WSClient client = WSClientFactory.getClient("util-hasTerminator");

			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("number1", "1");
			parameters.put("number2", "2");

			String path = "test/sumPost";

			String response = client.execPost(path, parameters).getOutput();
			Assert.assertEquals("3", response);

		} catch (Throwable e) {
			throw e;
		}
	}

	@Test
	public void simpleTest() throws WSClientException {
		WSClient wsclient = WSClientFactory.getClient("get");

		String out = wsclient.execGet("").getOutput();

		Assert.assertTrue(out.contains("Host"));
		Assert.assertTrue(out.contains("httpbin.org"));

	}
 
	public void execPostUnknownURL() throws WSClientException, InterruptedException {

		WSClient client = WSClientFactory.getClient("util");

		String path = "test/sum2/";
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("number1", 1);
		parameters.put("number2", 2);

		WSReponse response = client.execPost(path, parameters);

		Assert.assertEquals(Integer.valueOf(404), response.getCode());
	}

}
