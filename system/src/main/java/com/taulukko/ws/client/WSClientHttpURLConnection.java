package com.taulukko.ws.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class WSClientHttpURLConnection extends WSClient {

	private static final String ACCEPT_LANGUAGE = "en-US,en;q=0.5";
	private final String USER_AGENT = "Mozilla/5.0";

	public WSClientHttpURLConnection(String urlservice) {

		super(urlservice);
	}

	public WSReponse execPost(String url, Map<String, Object> parameters) throws WSClientException {
		try {
			java.lang.System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");

			url = prepareURL(url);

			HttpURLConnection con = buildHttpURLConnection(url);

			con.setRequestMethod("POST");

			addRequestHeadProperties(con);

			String urlParameters = parametersToQueryString(parameters);

			writeReqiestParameters(con, urlParameters);

			int responseCode = con.getResponseCode();

			String responseOutput = extractOutput(con);
			WSReponse response = new WSReponse();
			response.setCode(responseCode);
			response.setOutput(responseOutput);

			return response;
		} catch (Throwable e) {
			throw new WSClientException(e);
		}
	}

	public WSReponse execPost(String path) throws WSClientException {

		return execPost(path, new HashMap<>());
	}

	public WSReponse execGet(String url) throws WSClientException {
		try {

			url = prepareURL(url);

			HttpURLConnection con = buildHttpURLConnection(url);

			// optional default is GET
			con.setRequestMethod("GET");

			addRequestHeadProperties(con);

			int responseCode = con.getResponseCode();
			String responseOutput = extractOutput(con);
			WSReponse response = new WSReponse();
			response.setCode(responseCode);
			response.setOutput(responseOutput);

			return response;

		} catch (IOException e) {
			throw new WSClientException(e);
		}
	}

	/**
	 * Convert parameters in query parameters
	 */
	public WSReponse execGet(String path, Map<String, Object> parameters) throws WSClientException {
		try {
			boolean havePreviousParameters = path.contains("?");
			boolean haveMoreParameters = parameters.size() > 0;

			if (!havePreviousParameters && haveMoreParameters) {
				path += "?";
			}

			String query = parametersToQueryString(parameters);

			if (havePreviousParameters && haveMoreParameters) {
				path += "&";
			}

			return execGet(path + query);
		} catch (Throwable e) {
			throw new WSClientException(e);
		}

	}

	public WSReponse exec(String path, Map<String, Object> parameters, boolean post) throws WSClientException {
		if (post) {
			return execPost(path, parameters);
		} else {
			return execGet(path, parameters);
		}
	}

	private String parametersToQueryString(Map<String, Object> parameters) throws UnsupportedEncodingException {
		String query = "";

		boolean firstParameter = true;
		for (String parameter : parameters.keySet()) {
			if (!firstParameter) {
				query += "&";
			}
			query += URLEncoder.encode(parameter, "UTF-8") + "="
					+ URLEncoder.encode(parameters.get(parameter).toString(), "UTF-8");
			firstParameter = false;
		}

		return query;
	}

	private void addRequestHeadProperties(HttpURLConnection con) {
		// add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", ACCEPT_LANGUAGE);
	}

	private HttpURLConnection buildHttpURLConnection(String url) throws MalformedURLException, IOException {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		return con;
	}

	private void writeReqiestParameters(HttpURLConnection con, String urlParameters) throws IOException {
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();
	}

	private String extractOutput(HttpURLConnection con) throws IOException {

		InputStream input = null;

		if (con.getResponseCode() == 200) {
			input = con.getInputStream();
		} else {
			input = con.getErrorStream();
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(input));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		return response.toString();

	}

	private String prepareURL(String url) {
		url = "/" + StringUtils.removeStart(url, "/");
  
		return url;
	}

}
