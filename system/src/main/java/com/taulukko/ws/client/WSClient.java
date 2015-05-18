package com.taulukko.ws.client;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.http.client.HttpClient;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;

public class WSClient {

	private static final String USER_AGENT = "Mozilla/5.0";
	private HttpClient client = null;
	private String urlservice = null;

	public WSClient(String urlservice) {

		this.client = HttpClientBuilder.create()
				.setRedirectStrategy(new LaxRedirectStrategy())
				.setUserAgent(USER_AGENT).build();

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
