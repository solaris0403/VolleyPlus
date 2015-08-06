package com.tony.volleydemo.http;

/**
 * @author Tony E-mail:solaris0403@gmail.com
 * @version Create Data：Jul 29, 2015 6:19:18 PM
 */
public class VolleyError extends Exception {
	private static final long serialVersionUID = 1L;
	public final NetworkResponse networkResponse;
	private long networkTimeMs;

	public VolleyError() {
		networkResponse = null;
	}

	public VolleyError(NetworkResponse response) {
		networkResponse = response;
	}

	public VolleyError(String exceptionMessage) {
		super(exceptionMessage);
		networkResponse = null;
	}

	public VolleyError(String exceptionMessage, Throwable reason) {
		super(exceptionMessage, reason);
		networkResponse = null;
	}

	public VolleyError(Throwable cause) {
		super(cause);
		networkResponse = null;
	}

	/* package */void setNetworkTimeMs(long networkTimeMs) {
		this.networkTimeMs = networkTimeMs;
	}

	public long getNetworkTimeMs() {
		return networkTimeMs;
	}
}
