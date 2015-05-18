package com.taulukko.ws.client;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;

public class WSClient {

	private String urlservice = null;

	public WSClient(String urlservice) {

		if (urlservice.endsWith("/")) {
			this.urlservice = urlservice.replaceAll("/$", "");
		} else {
			this.urlservice = urlservice;
		}

	}

	public String execPost(String path, Map<String, Object> parameters)
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

		url = urlservice + "/" + path;

		boolean needTerminator = !url.endsWith("/") && !url.contains("?");
		// separator is required against redirect error
		if (needTerminator) {
			url += "/";
		}
		try {
			if (post) {

				Form form = Form.form();
				Set<String> keys = parameters.keySet();
				for (String key : keys) {
					form = form.add(key, parameters.get(key).toString());
				}

				return Request.Post(url).bodyForm(form.build()).execute()
						.returnContent().asString();

			} else {

				return Request.Get(url).execute().returnContent().asString();

			}
		} catch (IOException e) {
			throw new WSClientException(e.getMessage(), e);
		}
	}
}
