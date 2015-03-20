package integration.com.taulukko.commons.ws.runner;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.taulukko.ws.client.RESTClientFactory;
import com.taulukko.ws.client.WSClient;
import com.taulukko.ws.client.WSClientException;

public class HelloIntegrationTest {

	private static Tomcat tomcat = null;
	private static Thread thread = null;

	@BeforeClass
	public static void init() throws Exception {

		thread = new Thread(new Runnable() {

			@Override
			public void run() {
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
					tomcat.addWebapp("/",
							new File(webappDirLocation).getAbsolutePath());

					System.out.println("configuring app with basedir: "
							+ new File(webappDirLocation).getAbsolutePath());

					tomcat.start();
					tomcat.getServer().await();
				} catch (ServletException | LifecycleException
						| InterruptedException e) {
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
	public void execGet() throws WSClientException, InterruptedException {

		WSClient client = RESTClientFactory.getClient("util");

		// caso queira configurar mais, pode-se passar o formato e a versao aqui
		String path = "test/sum";
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("number1", 1);
		parameters.put("number2", 2);

		System.out.print("Teste com REST:\n");

		String output = client.execGet(path + "?number1=1&number2=2");

		Assert.assertTrue(output.equals("3"));

	}

	@Test
	public void execPost() throws WSClientException, InterruptedException {

		WSClient client = RESTClientFactory.getClient("util");

		// caso queira configurar mais, pode-se passar o formato e a versao aqui
		String path = "test/sum";
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("number1", 1);
		parameters.put("number2", 2);

		System.out.print("Teste com REST:\n");

		String output = client.execGet(path + "?number1=1&number2=2");

		Assert.assertTrue(output.equals("3"));

	}

}
