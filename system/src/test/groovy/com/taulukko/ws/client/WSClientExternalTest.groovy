package com.taulukko.ws.client;


import static org.junit.Assert.*

import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import com.google.gson.Gson
import com.google.gson.GsonBuilder

import groovyx.net.http.RESTClient

@RunWith(JUnit4.class)
class WSClientExternalTest extends GroovyTestCase{

 	
	@Test
	public void execGet() throws WSClientException,
	InterruptedException {

		def url = new URL("https://httpbin.org/get"  );


		URI uri = new URI(url.getProtocol(), null,url.getHost(), url.getPort(), url.getPath(), null , null);

		RESTClient restClient = new RESTClient(uri);

		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("number1", "1");
		parameters.put("number2", "2");

		Gson  gson = new GsonBuilder().create();
		def json = gson.toJson(parameters);

		def response = restClient.get([query:parameters] );


		assert response.success

		assert response.status == 200

		println response.data

	}
	@Test
	public void execPost() throws WSClientException,
	InterruptedException {

		def url = new URL("https://httpbin.org/post"  );


		URI uri = new URI(url.getProtocol(), null,url.getHost(), url.getPort(), url.getPath(), null , null);

		RESTClient restClient = new RESTClient(uri);

		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("number1", "1");
		parameters.put("number2", "2");

		Gson  gson = new GsonBuilder().create();
		def json = gson.toJson(parameters);

		def response = restClient.post(null, contentType:  groovyx.net.http.ContentType.JSON,
		requestContentType: groovyx.net.http.ContentType.JSON,
		body:json);


		assert response.success

		assert response.status == 200
		
		assert response.data.data == json



	}



}
