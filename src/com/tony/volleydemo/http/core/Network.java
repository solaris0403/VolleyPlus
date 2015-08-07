package com.tony.volleydemo.http.core;

/**
 * An interface for performing requests.
 * 
 * @author Tony E-mail:solaris0403@gmail.com
 * @version Create Data：Aug 6, 2015 9:15:04 AM
 */
public interface Network {
	/**
	 * Performs the specified request.
	 * 
	 * @param request
	 *            Request to process
	 * @return A {@link NetworkResponse} with data and caching metadata; will
	 *         never be null
	 * @throws VolleyError
	 *             on errors
	 */
	public NetworkResponse performRequest(Request<?> request) throws VolleyError;
}
