package com.taulukko.ws.client;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public abstract class WSClient extends WSBase {

	String urlservice = null;
	boolean hasTerminator = false;

	public WSClient(String urlservice, boolean hasTerminator) {

		if (urlservice.endsWith("/")) {
			urlservice = StringUtils.removeEnd(urlservice, "/");
		}
		this.urlservice = urlservice;
		this.hasTerminator = hasTerminator;

	}

	public abstract WSReponse execPost(String path, Map<String, Object> parameters) throws WSClientException;

	public abstract WSReponse execGet(String path) throws WSClientException;

	public abstract WSReponse execPost(String path ) throws WSClientException;

	/**
	 * Convert parameters in query parameters
	 */
	public abstract WSReponse execGet(String path, Map<String, Object> parameters) throws WSClientException;

	public abstract WSReponse exec(String path, Map<String, Object> parameters, boolean post) throws WSClientException;

}
