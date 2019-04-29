package com.taulukko.ws.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class WSClient {

	private String urlservice = null;

	public WSClient(String urlservice) {

		if (urlservice.endsWith("/")) {
			this.urlservice = urlservice.replaceAll("/$", "");
		} else {
			this.urlservice = urlservice;
		}

	}

	public WSReponse execPost(String path, Map<String, Object> parameters) throws WSClientException {
		return exec(path, parameters, true);
	}

	public WSReponse execGet(String path) throws WSClientException {
		return exec(path, null, false);
	}

	/**
	 * Convert parameters in query parameters
	 */
	public WSReponse execGet(String path, Map<String, Object> parameters) throws WSClientException {
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

	public WSReponse exec(String path, Map<String, Object> parameters, boolean post) throws WSClientException {
		
		WSReponse response = new WSReponse();

		String url = urlservice;

		if (path.startsWith("/")) {
			path = path.substring(1);
		}

		url = urlservice + "/" + path;

		boolean haveQueryParameters = url.contains("?");
		boolean needTerminator = !url.endsWith("/") && !haveQueryParameters;
		needTerminator = needTerminator || (haveQueryParameters && !url.contains("/?"));

		// separator is required against redirect error
		if (needTerminator) {
			if (!haveQueryParameters) {
				url += "/";
			} else {
				url = StringUtils.replace(url, "?", "/?");
			}
		}

		try {

			URL newurl = new URL(url);

			HttpURLConnection conn = (HttpURLConnection) newurl.openConnection();

			conn.setReadTimeout(10000);
			conn.setConnectTimeout(15000);
			if (post) {
				// change default method GET to POST
				conn.setRequestMethod("POST");
			} else {
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				StringBuilder sb = new StringBuilder();
				String output;
				while ((output = br.readLine()) != null) {
					sb.append(output);
				}
				response.setOutput(  sb.toString());
				return response;
			}
			conn.setDoInput(true);
			conn.setDoOutput(true);

			List<NameValuePair> params = new ArrayList<NameValuePair>();

			if (parameters != null) {
				Set<String> keys = parameters.keySet();

				for (String key : keys) {
					params.add(new BasicNameValuePair(key, parameters.get(key).toString()));
				}
			}

			OutputStream os = conn.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
			writer.write(getQuery(params));
			writer.flush();
			writer.close();
			os.close();

			conn.connect();

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			StringBuilder sb = new StringBuilder();
			String output;
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}

			response.setOutput(  sb.toString());
			return response;

		} catch (IOException e) {
			throw new WSClientException(e.getMessage(), e);
		}
	}

	private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
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
