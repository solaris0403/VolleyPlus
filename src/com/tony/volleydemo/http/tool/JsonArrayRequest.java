package com.tony.volleydemo.http.tool;

import java.io.UnsupportedEncodingException;

import org.json.JSONArray;
import org.json.JSONException;

import com.tony.volleydemo.http.core.NetworkResponse;
import com.tony.volleydemo.http.core.ParseError;
import com.tony.volleydemo.http.core.Response;
import com.tony.volleydemo.http.core.Response.ErrorListener;
import com.tony.volleydemo.http.core.Response.Listener;

/**
 * @author Tony E-mail:solaris0403@gmail.com
 * @version Create Data：Aug 7, 2015 3:55:12 PM
 */
public class JsonArrayRequest extends JsonRequest<JSONArray> {
	/**
	 * Creates a new request.
	 * 
	 * @param url
	 *            URL to fetch the JSON from
	 * @param listener
	 *            Listener to receive the JSON response
	 * @param errorListener
	 *            Error listener, or null to ignore errors.
	 */
	public JsonArrayRequest(String url, Listener<JSONArray> listener, ErrorListener errorListener) {
		super(Method.GET, url, null, listener, errorListener);
	}

	/**
	 * Creates a new request.
	 * 
	 * @param method
	 *            the HTTP method to use
	 * @param url
	 *            URL to fetch the JSON from
	 * @param jsonRequest
	 *            A {@link JSONArray} to post with the request. Null is allowed
	 *            and indicates no parameters will be posted along with request.
	 * @param listener
	 *            Listener to receive the JSON response
	 * @param errorListener
	 *            Error listener, or null to ignore errors.
	 */
	public JsonArrayRequest(int method, String url, JSONArray jsonRequest, Listener<JSONArray> listener, ErrorListener errorListener) {
		super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), listener, errorListener);
	}

	@Override
	protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
		try {
			String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
			return Response.success(new JSONArray(jsonString), HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JSONException je) {
			return Response.error(new ParseError(je));
		}
	}
}
