package com.taulukko.ws.client;

import org.apache.commons.lang3.StringUtils
import org.apache.http.NameValuePair

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.taulukko.ws.client.traits.WSBase

import groovyx.net.http.RESTClient

public class WSClient extends WSBase {

	private String urlservice = null;

	public WSClient(String urlservice) {

		if (urlservice.endsWith("/")) {
			this.urlservice = urlservice.replaceAll("/\$", "");
		} else {
			this.urlservice = urlservice;
		}
	}

	public  String execPost(String path, Map<String, Object> parameters)
	throws WSClientException {
		return exec(path, parameters, true);
	}

	public String execGet(String path) throws WSClientException {
		return exec(path, null, false);
	}

	/**
	 * Convert parameters in query parameters
	 * */
	public String execGet(String path, Map<String, Object> parameters)
	throws WSClientException {
		String parametersQuery = "?";
		boolean first = true;

		if (path.contains("?")) {
			parametersQuery = "";
			first = false;
		}
		else {
			def cfg = this.config;
			
			if( cfg.properties.endsWithSeparator && !path.endsWith("/") && !path.endsWith("\\") ) {
				path+="/";
			}
		}

		Set<String> keys = parameters.keySet();

		for (String key : keys) {
			if (!first) {
				parametersQuery += "&";
			}
			parametersQuery += key + "=" + parameters.get(key).toString();
			first = false;
		}

		return exec(path + parametersQuery, null, false);
	}

	public String exec(String path, Map<String, Object> parameters, boolean post)
	throws WSClientException {

		String url = urlservice;

		if (path.startsWith("/")) {
			path = path.substring(1);
		}

		
		println " config properties:${this.config.properties}"
		println "endsWithSeparator:${this.config.properties.endsWithSeparator}"
			
		boolean needTerminator = !path.endsWith("/") && !path.contains("?") && this.config.properties.endsWithSeparator ;
		
		println "needTerminator:${needTerminator}"
		
		// separator is required against redirect error
		if (needTerminator) {
			path += "/";
		}

		if(path!="") {
			url = urlservice + "/" + path;
		}

		
		try {

			URL newurl = new URL(url);


			println  "urlToUri=${newurl}";
			println  "path=${path}";
			println  "urlservice=${urlservice}";
			println  "post=${post}";

			if (post) {
 
				//System.out.println("hgora de testar");
				//Thread.sleep(10000);

				//TODO trocar a biblioteca pra post e get

				//"number1=1&number2=2"
				URI uri = new URI(newurl.getProtocol(), null,newurl.getHost(), newurl.getPort(), newurl.getPath(), null , null);

				RESTClient restClient = new RESTClient(uri);

				//TODO: Criar no parsers um que converte de mapa pra json
				Gson  gson = new GsonBuilder().create();
				def json = gson.toJson(parameters);


				println  "parameters:${json}"
				println uri.toString()

				def response = restClient.post(null, contentType:  groovyx.net.http.ContentType.JSON,
				requestContentType: groovyx.net.http.ContentType.URLENC,
				body:parameters);


				println  "response=${response}"

				if( response.status != 200)
				{
					throw new Exception(response.data , respose.status);
				}

				println  "status=${response.status}"
				println  "data=${response.data}"


				return response.data;

			}
			HttpURLConnection conn = (HttpURLConnection) newurl
					.openConnection();

			conn.setReadTimeout(10000);
			conn.setConnectTimeout(15000);

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			StringBuilder sb = new StringBuilder();
			String output;
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}
			return sb.toString();


		} catch (Throwable e) {
			//debug
			def message = "Error in WSClient [${path}] , try change property terminator url ";
			message += StringUtils.defaultIfEmpty(e.getMessage(),"Error in WSClient.post") + " : ";
			def cause = e.getCause()
			message += (cause)?cause.getMessage():"No previous reason";
			throw new WSClientException(message, e);
		}
	}

	private String getQuery(List<NameValuePair> params)
	throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();
		boolean first = true;

		for (NameValuePair pair : params) {
			if (first)
				first = false;
			else
				result.append("&");

			result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
		}

		return result.toString();
	}
}
