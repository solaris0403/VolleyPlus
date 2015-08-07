package com.tony.volleydemo.http.core;
/**
 * @author Tony E-mail:solaris0403@gmail.com
 * @version Create Data：Aug 7, 2015 2:15:16 PM
 */
@SuppressWarnings("serial")
public class ServerError extends VolleyError{
	public ServerError(NetworkResponse networkResponse) {
        super(networkResponse);
    }

    public ServerError() {
        super();
    }
}
