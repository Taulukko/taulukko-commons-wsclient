package com.taulukko.ws.client;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.io.IOUtils
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.gson.Gson
import com.google.gson.GsonBuilder



import static org.junit.Assert.*
   
import com.taulukko.commons.util.io.EFile


import groovyx.net.http.RESTClient 
import groovy.json.*

@RunWith(JUnit4.class)
public class WSClientTest extends WSBaseTest{

	private static Tomcat tomcat = null;
	private static Thread thread = null;

	@BeforeClass
	public static void init() throws Exception {

		thread = new Thread(new Runnable() {

					@Override
					public void run() {
						String webappDirLocation = "src/main/resources/webapps";
						try {
							WSClientFactory.start(new File(".").getAbsolutePath() + "/"
									+ webappDirLocation + "/WEB-INF/classes");
						} catch (Exception e1) { 
							e1.printStackTrace();
						}
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
	public void execGetDirectQuery() throws WSClientException,
	InterruptedException {

		WSClient client = WSClientFactory.getClient("util");

		String path = "test/sum/";

		String output = client.execGet(path + "?number1=1&number2=2");

		assert "3" ==   output;

	}

	@Test
	public void execGet() throws WSClientException, InterruptedException {

		WSClient client = WSClientFactory.getClient("util");

		String path = "test/sum/";
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("number1", 1);
		parameters.put("number2", 2);

		String output = client.execGet(path, parameters);

		assert "3" ==   output;
		 
	}

	@Test
	public void execGetWithoutTerminatorSuccess() throws WSClientException,
	InterruptedException {

		WSClient client = WSClientFactory.getClient("util");

		String path = "test/sum";
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("number1", 1);
		parameters.put("number2", 2);

		String output = client.execGet(path, parameters);

		assert "3" == output

	}
	 
	@Test
	public void execGetMultiIn() throws WSClientException, InterruptedException {

		WSClient client = WSClientFactory.getClient("util");

		String path = "test/sum/";
		Map<String, Object> parameters = new HashMap<String, Object>();

		
		parameters.put("number2", 2);

		String output = client.execGet(path + "?number1=1", parameters);

		assert "3" ==  output;

	}
 

	 
	@Test(expected=Throwable)
	public void execPostWithoutTerminatorError() throws WSClientException,
	InterruptedException {
 
		WSClient client = WSClientFactory.getClient("util");

		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("number1", "1");
		parameters.put("number2", "2");
 
		String path = "test/sumPost";

		def response = client.execPost(path,parameters);

		assert response == "3"; 

	}
 
	
	@Test
	public void execPostWithTerminator() throws WSClientException,
	InterruptedException {
 
		WSClient client = WSClientFactory.getClient("util");

		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("number1", "1");
		parameters.put("number2", "2");
 
		String path = "test/sumPost/";

		def response = client.execPost(path,parameters);

		assert response == "3";
 

	}
	 
	@Test
	public void execPostWithoutTerminatorSuccess() throws WSClientException,
	InterruptedException {

		
		try{

			config.properties.endsWithSeparator=true;
 			WSClient client = WSClientFactory.getClient("util");		 
	
			Map<String, Object> parameters = new HashMap<String, Object>();
	
			parameters.put("number1", "1");
			parameters.put("number2", "2");
	 
			String path = "test/sumPost";
	
			def response = client.execPost(path,parameters);	
			assert response == "3";
	
		}
		catch(any)
		{
			throw any;
		}
		finally
		{
			config.properties.endsWithSeparator=false;

		}

	}
 


	@Test
	public void simpleTest() {
		WSClient wsclient =  WSClientFactory.getClient("get");

		String out = wsclient.execGet("");

		assert out.contains  ('Host=httpbin.org');
	}
 

	@Test(expected = WSClientException.class)
	public void execPostUnknownURL() throws WSClientException,
	InterruptedException {

		WSClient client = WSClientFactory.getClient("util");
 
		String path = "test/sum2/";
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("number1", 1);
		parameters.put("number2", 2);

		String output = client.execPost(path, parameters);

		assert "3" == output;
	}

}
