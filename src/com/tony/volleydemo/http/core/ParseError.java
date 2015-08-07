package com.tony.volleydemo.http.core;

/**
 * @author Tony E-mail:solaris0403@gmail.com
 * @version Create Data：Aug 7, 2015 2:14:30 PM
 */
@SuppressWarnings("serial")
public class ParseError extends VolleyError {
	public ParseError() {
	}

	public ParseError(NetworkResponse networkResponse) {
		super(networkResponse);
	}

	public ParseError(Throwable cause) {
		super(cause);
	}
}
