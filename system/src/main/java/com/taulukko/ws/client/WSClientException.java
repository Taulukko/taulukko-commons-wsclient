package com.taulukko.ws.client;

public class WSClientException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1439159452956049283L;

	public static final Integer CLIENT_ERROR = -1;
	
	private Integer code = 0;

	public WSClientException(String message) {
		super(message);
	}

	public WSClientException(String message, Integer code) {
		super(message);
		this.code = code;
	}

	public WSClientException(String message, Throwable t) {
		super(message, t);
	}

	public WSClientException(String message, Integer code, Throwable t) {
		super(message, t);
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
}
