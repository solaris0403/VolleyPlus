package com.tony.volleydemo.http.core;

import android.content.Intent;

/**
 * Error indicating that there was an authentication failure when performing a
 * Request.
 * 
 * @author Tony E-mail:solaris0403@gmail.com
 * @version Create Data：Aug 4, 2015 2:47:12 PM
 */
@SuppressWarnings("serial")
public class AuthFailureError extends VolleyError {
	/**
	 * An intent that can be used to resolve this exception. (Brings up the
	 * password dialog.)
	 */
	private Intent mResolutionIntent;

	public AuthFailureError() {
	}

	public AuthFailureError(Intent intent) {
		mResolutionIntent = intent;
	}

	public AuthFailureError(NetworkResponse response) {
		super(response);
	}

	public AuthFailureError(String message) {
		super(message);
	}

	public AuthFailureError(String message, Exception reason) {
		super(message, reason);
	}

	public Intent getResolutionIntent() {
		return mResolutionIntent;
	}

	@Override
	public String getMessage() {
		if (mResolutionIntent != null) {
			return "User needs to (re)enter credentials.";
		}
		return super.getMessage();
	}
}
