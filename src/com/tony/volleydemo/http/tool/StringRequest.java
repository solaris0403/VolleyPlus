package com.tony.volleydemo.http.tool;

import java.io.UnsupportedEncodingException;

import com.tony.volleydemo.http.core.NetworkResponse;
import com.tony.volleydemo.http.core.Request;
import com.tony.volleydemo.http.core.Response;
import com.tony.volleydemo.http.core.Response.ErrorListener;
import com.tony.volleydemo.http.core.Response.Listener;

/**
 * @author Tony E-mail:solaris0403@gmail.com
 * @version Create Data：Aug 7, 2015 3:34:14 PM
 */
public class StringRequest extends Request<String> {
	private final Listener<String> mListener;

	/**
	 * Creates a new request with the given method.
	 *
	 * @param method
	 *            the request {@link Method} to use
	 * @param url
	 *            URL to fetch the string at
	 * @param listener
	 *            Listener to receive the String response
	 * @param errorListener
	 *            Error listener, or null to ignore errors
	 */
	public StringRequest(int method, String url, Listener<String> listener, ErrorListener errorListener) {
		super(method, url, errorListener);
		mListener = listener;
	}

	/**
	 * Creates a new GET request.
	 *
	 * @param url
	 *            URL to fetch the string at
	 * @param listener
	 *            Listener to receive the String response
	 * @param errorListener
	 *            Error listener, or null to ignore errors
	 */
	public StringRequest(String url, Listener<String> listener, ErrorListener errorListener) {
		this(Method.GET, url, listener, errorListener);
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		String parsed;
		try {
			parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
		} catch (UnsupportedEncodingException e) {
			parsed = new String(response.data);
		}
		return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
	}

	@Override
	protected void deliverResponse(String response) {
		mListener.onResponse(response);

	}

}
