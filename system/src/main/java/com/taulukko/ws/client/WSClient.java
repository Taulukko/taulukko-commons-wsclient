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

	public abstract String execPost(String path, Map<String, Object> parameters) throws WSClientException;

	public abstract String execGet(String path) throws WSClientException;

	public abstract String execPost(String path ) throws WSClientException;

	/**
	 * Convert parameters in query parameters
	 */
	public abstract String execGet(String path, Map<String, Object> parameters) throws WSClientException;

	public abstract String exec(String path, Map<String, Object> parameters, boolean post) throws WSClientException;

}
