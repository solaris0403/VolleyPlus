package com.tony.volleydemo.http;

/**
 * @author Tony E-mail:solaris0403@gmail.com
 * @version Create Data：Aug 6, 2015 9:16:15 AM
 */
public interface ResponseDelivery {
	/**
	 * Parses a response from the network or cache and delivers it.
	 */
	public void postResponse(Request<?> request, Response<?> response);

	/**
	 * Parses a response from the network or cache and delivers it. The provided
	 * Runnable will be executed after delivery.
	 */
	public void postResponse(Request<?> request, Response<?> response, Runnable runnable);

	/**
	 * Posts an error for the given request.
	 */
	public void postError(Request<?> request, VolleyError error);
}
