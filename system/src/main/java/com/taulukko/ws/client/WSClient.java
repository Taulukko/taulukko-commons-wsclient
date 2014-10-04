package com.taulukko.ws.client;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class WSClient {
	private HttpClient client = null;
	private String urlservice = null;

	public WSClient(String urlservice) {
		this.client = HttpClientBuilder.create().build();

		if (urlservice.endsWith("/")) {
			this.urlservice = urlservice.replaceAll("/$", "");
		} else {
			this.urlservice = urlservice;
		}

	}

	public String execPost(String path,
			Map<String, Object> parameters) throws WSClientException {
		return exec(path, parameters, true);
	}

	public String execGet(String path) throws WSClientException {
		return exec(path, null, false);
	}

	public String exec(String path, Map<String, Object> parameters,
			boolean post) throws WSClientException {

		String url = urlservice;

		url = urlservice + "/" + path;
 

		if (post) {
			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

			for (String parameter : parameters.keySet()) {
				nameValuePairs.add(new BasicNameValuePair(parameter, parameters
						.get(parameter).toString()));
			}

			// nameValuePairs.add(new BasicNameValuePair("appKey", appKey));
			// nameValuePairs.add(new BasicNameValuePair("output",
			// format.toString()));

			try {
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			} catch (UnsupportedEncodingException e1) {
				throw new WSClientException("Error : " + e1.getMessage(),
						WSClientException.CLIENT_ERROR, e1);
			}

			HttpResponse response1 = null;
			try {
				response1 = this.client.execute(httpPost);

				HttpEntity entity1 = response1.getEntity();
				// do something useful with the response body
				// and ensure it is fully consumed
				String s = EntityUtils.toString(entity1);
				EntityUtils.consume(entity1);

				if (response1.getStatusLine().getStatusCode() != 200) {
					throw new WSClientException("Error "
							+ response1.getStatusLine().toString()
							+ " content: " + s, WSClientException.CLIENT_ERROR);
				}
				return s;
			} catch (WSClientException wse) {
				throw wse;

			} catch (Exception e) {
				throw new WSClientException("Error " + e.getMessage(),
						WSClientException.CLIENT_ERROR, e);

			} finally {
				httpPost.releaseConnection();
			}

		}

		HttpGet httpGet = new HttpGet(url);
		HttpResponse response1 = null;
		try {
			response1 = this.client.execute(httpGet);

			HttpEntity entity1 = response1.getEntity();
			// do something useful with the response body
			// and ensure it is fully consumed
			String s = EntityUtils.toString(entity1);
			EntityUtils.consume(entity1);

			if (response1.getStatusLine().getStatusCode() != 200) {
				throw new WSClientException("Error "
						+ response1.getStatusLine().toString() + " content: "
						+ s, WSClientException.CLIENT_ERROR);
			}
			return s;
		} catch (WSClientException wse) {
			throw wse;

		} catch (Exception e) {
			throw new WSClientException("Error " + e.getMessage(),
					WSClientException.CLIENT_ERROR, e);

		} finally {
			httpGet.releaseConnection();

		}

	}

}
