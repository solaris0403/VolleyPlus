package com.tony.volleydemo.http.core;

import java.util.Collections;
import java.util.Map;

import org.apache.http.HttpStatus;

/**
 * Data and headers returned from {@link Network#performRequest(Request)}.
 * 
 * @author Tony E-mail:solaris0403@gmail.com
 * @version Create Data：Jul 30, 2015 9:08:05 AM
 */
public class NetworkResponse {
	/** The HTTP status code. */
	public final int statusCode;

	/** Raw data from this response. */
	public final byte[] data;

	/** Response headers. */
	public final Map<String, String> headers;

	/** True if the server returned a 304 (Not Modified). */
	public final boolean notModified;

	/** Network roundtrip time in milliseconds. */
	public final long networkTimeMs;

	public NetworkResponse(byte[] data) {
		this(HttpStatus.SC_OK, data, Collections.<String, String> emptyMap(), false, 0);
	}

	public NetworkResponse(byte[] data, Map<String, String> headers) {
		this(HttpStatus.SC_OK, data, headers, false, 0);
	}

	public NetworkResponse(int statusCode, byte[] data, Map<String, String> headers, boolean notModified) {
		this(statusCode, data, headers, notModified, 0);
	}

	/**
	 * Creates a new network response.
	 * 
	 * @param statusCode
	 * @param data
	 * @param headers
	 * @param notModified
	 * @param networkTimeMs
	 */
	public NetworkResponse(int statusCode, byte[] data, Map<String, String> headers, boolean notModified, long networkTimeMs) {
		this.statusCode = statusCode;
		this.data = data;
		this.headers = headers;
		this.notModified = notModified;
		this.networkTimeMs = networkTimeMs;
	}

}
