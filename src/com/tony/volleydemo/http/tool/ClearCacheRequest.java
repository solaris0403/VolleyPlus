package com.tony.volleydemo.http.tool;

import android.os.Handler;
import android.os.Looper;

import com.tony.volleydemo.http.core.Cache;
import com.tony.volleydemo.http.core.NetworkResponse;
import com.tony.volleydemo.http.core.Request;
import com.tony.volleydemo.http.core.Response;

/**
 * @author Tony E-mail:solaris0403@gmail.com
 * @version Create Data：Aug 10, 2015 8:40:30 AM
 */
public class ClearCacheRequest extends Request<Object> {
	private final Cache mCache;
	private final Runnable mCallback;

	/**
	 * Creates a synthetic request for clearing the cache.
	 * 
	 * @param cache
	 *            Cache to clear
	 * @param callback
	 *            Callback to make on the main thread once the cache is clear,
	 *            or null for none
	 */
	public ClearCacheRequest(Cache cache, Runnable callback) {
		super(Method.GET, null, null);
		mCache = cache;
		mCallback = callback;
	}

	@Override
	public boolean isCanceled() {
		// This is a little bit of a hack, but hey, why not.
		mCache.clear();
		if (mCallback != null) {
			Handler handler = new Handler(Looper.getMainLooper());
			handler.postAtFrontOfQueue(mCallback);
		}
		return true;
	}

	@Override
	public Priority getPriority() {
		return Priority.IMMEDIATE;
	}

	@Override
	protected Response<Object> parseNetworkResponse(NetworkResponse response) {
		return null;
	}

	@Override
	protected void deliverResponse(Object response) {
	}

}
