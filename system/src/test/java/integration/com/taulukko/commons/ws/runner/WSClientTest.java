package integration.com.taulukko.commons.ws.runner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.taulukko.commons.util.io.EFile;
import com.taulukko.ws.client.WSClient;
import com.taulukko.ws.client.WSClientException;
import com.taulukko.ws.client.WSClientFactory;

public class WSClientTest {

	private static TestServer server = null;

	private static void handle(String target, Request baseRequest, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		switch (baseRequest.getRequestURI()) {
		case "/util/test/sum/": {

			Map<String, String[]> parameters = baseRequest.getParameterMap();

			String number1[] = parameters.get("number1");
			String number2[] = parameters.get("number2");

			System.out.println(baseRequest.getParameterMap());
			System.out.println(baseRequest.getParameterNames());

			response.setContentType("application/json");
			response.setStatus(HttpServletResponse.SC_OK);
			baseRequest.setHandled(true);
			response.getWriter().println( String.valueOf(Integer.valueOf(number1[0]) + Integer.valueOf(number2[0])));
			return;
		}
		default: {
			System.out.println("URI"+baseRequest.getRequestURI());
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		}

	}

	@BeforeClass
	public static void init() throws Exception {
		WSClientFactory.start(EFile.getClassLoaderPath(), false);
		
		server = new TestServer();
		server.start(new FunctionalHandler(WSClientTest::handle));
	}

	@AfterClass
	public static void end() throws Exception {
		server.stop();
	}

	@Test
	public void execGetDirectQuery() throws WSClientException, InterruptedException {

		WSClient client = WSClientFactory.getClient("util");

		String path = "test/sum/";
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("number1", 1);
		parameters.put("number2", 2);

		String output = client.execGet(path + "?number1=1&number2=2");

		Assert.assertEquals("3", output);

	}

	@Test
	public void execGet() throws WSClientException, InterruptedException {

		WSClient client = WSClientFactory.getClient("util");

		String path = "test/sum/";
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("number1", 1);
		parameters.put("number2", 2);

		String output = client.execGet(path, parameters);

		Assert.assertEquals("3", output);

	}

	@Test
	public void execGetWithoutTerminator() throws WSClientException, InterruptedException {

		WSClient client = WSClientFactory.getClient("util");

		String path = "test/sum";
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("number1", 1);
		parameters.put("number2", 2);

		String output = client.execGet(path, parameters);

		Assert.assertEquals("3", output);

	}

	@Test
	public void execGetMultiIn() throws WSClientException, InterruptedException {

		WSClient client = WSClientFactory.getClient("util");

		String path = "test/sum/";
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("number2", 2);

		String output = client.execGet(path + "?number1=1", parameters);

		Assert.assertEquals("3", output);

	}

	@Test
	public void execPostWithoutTerminator() throws WSClientException, InterruptedException {

		WSClient client = WSClientFactory.getClient("util");

		// caso queira configurar mais, pode-se passar o formato e a versao aqui
		String path = "test/sum";
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("number1", 1);
		parameters.put("number2", 2);

		String output = client.execPost(path, parameters);

		Assert.assertEquals("3", output);
	}

	@Test
	public void execPost() throws WSClientException, InterruptedException {

		WSClient client = WSClientFactory.getClient("util");

		// caso queira configurar mais, pode-se passar o formato e a versao aqui
		String path = "test/sum/";
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("number1", 1);
		parameters.put("number2", 2);

		String output = client.execPost(path, parameters);

		Assert.assertEquals("3", output);
	}

	@Test(expected = WSClientException.class)
	public void execPostUnknownURL() throws WSClientException, InterruptedException {

		WSClient client = WSClientFactory.getClient("util");

		// caso queira configurar mais, pode-se passar o formato e a versao aqui
		String path = "test/sum2/";
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("number1", 1);
		parameters.put("number2", 2);

		String output = client.execPost(path, parameters);

		Assert.assertEquals("3", output);
	}

}
