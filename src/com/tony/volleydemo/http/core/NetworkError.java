package com.tony.volleydemo.http.core;
/**
 * @author Tony E-mail:solaris0403@gmail.com
 * @version Create Data：Aug 7, 2015 2:12:49 PM
 */
@SuppressWarnings("serial")
public class NetworkError extends VolleyError{
	public NetworkError() {
        super();
    }

    public NetworkError(Throwable cause) {
        super(cause);
    }

    public NetworkError(NetworkResponse networkResponse) {
        super(networkResponse);
    }
}
