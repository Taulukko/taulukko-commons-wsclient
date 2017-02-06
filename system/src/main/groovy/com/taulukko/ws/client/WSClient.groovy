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
		return execGet(path, [:]);
	}

	/**
	 * Convert parameters in query parameters
	 * */
	public String execGet(String path, Map<String, Object> parameters)
	throws WSClientException {

		if(path.contains ("?") ){
			def pathPart = StringUtils.split( path,'?');
			def parametersQuery = StringUtils.split(pathPart[1], "&")
			for(def parameter : parametersQuery) {
				def parameterPart = parameter.split("=")
				parameters.put(parameterPart[0],parameterPart[1])
			}
			path = pathPart[0]
		}

		return exec(path, parameters, false);
		/*String parametersQuery = "?";
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
		 return exec(path + parametersQuery, null, false);*/
	}

	public String exec(String path, Map<String, Object> parameters, boolean post)
	throws WSClientException {

		String url = urlservice;

		if (path.startsWith("/")) {
			path = path.substring(1);
		}

		boolean needTerminator = !path.endsWith("/") && !path.contains("?") && this.config.properties.endsWithSeparator ;


		// separator is required against redirect error
		if (needTerminator) {
			path += "/";
		}

		if(path!="") {
			url = urlservice + "/" + path;
		}


		try {
			URL newurl = new URL(url);


			URI uri = new URI(newurl.getProtocol(), null,newurl.getHost(), newurl.getPort(), newurl.getPath(), null , null);

			//@TODO: Criar no parsers um que converte de mapa pra json
			Gson  gson = new GsonBuilder().create();
			def json = gson.toJson(parameters);


			RESTClient restClient = new RESTClient(uri);

			def response;

			if (post) {
				response = restClient.post(null, contentType:  groovyx.net.http.ContentType.JSON,
				requestContentType: groovyx.net.http.ContentType.URLENC,
				body:parameters);

			}
			else
			{

				response = restClient.get(["query":parameters]);
			}

			if( response.status != 200)
			{
				throw new Exception(response.data , respose.status);
			}


			return response.data.toString();

		} catch (Throwable e) {

			def message =(this.config.properties.endsWithSeparator)?"Error in WSClient [${path}] ":"Error in WSClient [${path}] , try change property terminator url ";
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
