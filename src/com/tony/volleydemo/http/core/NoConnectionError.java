package com.tony.volleydemo.http.core;
/**
 * @author Tony E-mail:solaris0403@gmail.com
 * @version Create Data：Aug 7, 2015 2:13:57 PM
 */
@SuppressWarnings("serial")
public class NoConnectionError extends NetworkError{
	 public NoConnectionError() {
	        super();
	    }

	    public NoConnectionError(Throwable reason) {
	        super(reason);
	    }
}
