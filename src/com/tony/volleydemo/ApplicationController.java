package com.tony.volleydemo;

import com.tony.volleydemo.http.core.RequestQueue;

import android.app.Application;

/**
 * @author Tony E-mail:solaris0403@gmail.com
 * @version Create Data：Aug 25, 2015 3:37:47 PM
 */
public class ApplicationController extends Application {
	/**
	 * Global request queue for Volley
	 */
	private RequestQueue mRequestQueue;
	/**
	 * A singleton instance of the application class for easy access in other
	 * places
	 */
	private static ApplicationController sInstance;
	@Override
	public void onCreate() {
		super.onCreate();
		// initialize the singleton
		sInstance = this;
	}

	/**
	 * @return ApplicationController singleton instance
	 */
	public static synchronized ApplicationController getInstance() {
		return sInstance;
	}
}
