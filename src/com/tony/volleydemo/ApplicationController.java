package com.tony.volleydemo;

import android.app.Application;

import com.tony.volleydemo.http.utils.VolleyPlus;

/**
 * @author Tony E-mail:solaris0403@gmail.com
 * @version Create Data：Aug 25, 2015 3:37:47 PM
 */
public class ApplicationController extends Application {
	private static ApplicationController sInstance;
	@Override
	public void onCreate() {
		super.onCreate();
		sInstance = this;
		VolleyPlus.init(this);
	}

	/**
	 * @return ApplicationController singleton instance
	 */
	public static synchronized ApplicationController getInstance() {
		return sInstance;
	}
}
