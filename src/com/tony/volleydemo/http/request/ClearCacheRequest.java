/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tony.volleydemo.http.request;

import android.os.Handler;
import android.os.Looper;

import com.tony.volleydemo.http.cache.Cache;
import com.tony.volleydemo.http.core.Listener;
import com.tony.volleydemo.http.core.NetworkResponse;
import com.tony.volleydemo.http.core.Request;
import com.tony.volleydemo.http.core.Response;

/**
 * A synthetic request used for clearing the cache.
 */
public class ClearCacheRequest extends Request<Object> {
	private final Listener<Object> mListener;
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
	public ClearCacheRequest(Cache cache, Runnable callback, Listener<Object> listener) {
		super(Method.GET, null, null);
		mCache = cache;
		mCallback = callback;
		mListener = listener;
	}

	@Override
	public boolean isCanceled() {
		mListener.onPreExecute();
		// This is a little bit of a hack, but hey, why not.
		mCache.clear();
		mListener.onSuccess(mCache);
		if (mCallback != null) {
			Handler handler = new Handler(Looper.getMainLooper());
			handler.postAtFrontOfQueue(mCallback);
		}
		mListener.onFinish();
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