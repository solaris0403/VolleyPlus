package com.tony.volleydemo.http.tool;

import com.tony.volleydemo.http.core.AuthFailureError;

/**
 * An interface for interacting with auth tokens.
 * 
 * @author Tony E-mail:solaris0403@gmail.com
 * @version Create Data：Aug 10, 2015 8:36:11 AM
 */
public interface Authenticator {
	/**
	 * Synchronously retrieves an auth token.
	 *
	 * @throws AuthFailureError
	 *             If authentication did not succeed
	 */
	public String getAuthToken() throws AuthFailureError;

	/**
	 * Invalidates the provided auth token.
	 */
	public void invalidateAuthToken(String authToken);
}
