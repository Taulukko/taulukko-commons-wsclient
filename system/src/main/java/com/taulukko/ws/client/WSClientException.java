package com.taulukko.ws.client;

import com.taulukko.commons.TaulukkoException;

public class WSClientException extends TaulukkoException {

	public static final Integer CLIENT_ERROR = -1;

	private Integer code = 0;

	public WSClientException(Throwable e) {
		super(e);
		this.code = -1;
	}

	public WSClientException(String message) {
		super(message);
	}

	public WSClientException(String message, Integer code) {
		super(message);
		this.code = code;
	}

	public WSClientException(Throwable t) {
		this(t.getMessage(), t);
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
