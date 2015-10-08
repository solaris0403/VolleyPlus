/*
 * Copyright (C) 2012 The Android Open Source Project
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

package com.tony.volleydemo.http.tool;

import java.io.File;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.http.AndroidHttpClient;
import android.os.Build;
import android.os.Environment;

import com.tony.volleydemo.http.cache.DiskCache;
import com.tony.volleydemo.http.core.Network;
import com.tony.volleydemo.http.core.RequestQueue;
import com.tony.volleydemo.http.stack.HttpClientStack;
import com.tony.volleydemo.http.stack.HttpStack;
import com.tony.volleydemo.http.stack.HurlStack;

public class Volley {

	/** Default on-disk cache directory. */
	private static final String DEFAULT_CACHE_DIR = "volley";

	/**
	 * Get cache dir
	 * @Title getDiskCacheDir
	 * @Description TODO
	 * @param context
	 * @param uniqueName
	 * @return
	 * @return File
	 */
	public static File getDiskCacheDir(Context context, String uniqueName) {
		String cachePath;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
			cachePath = context.getExternalCacheDir().getPath();
		} else {
			cachePath = context.getCacheDir().getPath();
		}
		return new File(cachePath + File.separator + uniqueName);
	}

	/**
	 * Creates a default instance of the worker pool and calls
	 * {@link RequestQueue#start()} on it. You may set a maximum size of the
	 * disk cache in bytes.
	 *
	 * @param context
	 *            A {@link Context} to use for creating the cache dir.
	 * @param stack
	 *            An {@link HttpStack} to use for the network, or null for
	 *            default.
	 * @param maxDiskCacheBytes
	 *            the maximum size of the disk cache, in bytes. Use -1 for
	 *            default size.
	 * @param isOpenCache
	 *            whether open disk cache.
	 * @return A started {@link RequestQueue} instance.
	 */
	public static RequestQueue newRequestQueue(Context context, HttpStack stack, int maxDiskCacheBytes, boolean isOpenCache) {
		//File cacheDir = new File(context.getCacheDir(), DEFAULT_CACHE_DIR);
		String userAgent = "volley/0";
		try {
			String packageName = context.getPackageName();
			PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
			userAgent = packageName + "/" + info.versionCode;
		} catch (NameNotFoundException e) {
		}
		if (stack == null) {
			if (Build.VERSION.SDK_INT >= 9) {
				stack = new HurlStack();
			} else {
				// Prior to Gingerbread, HttpUrlConnection was unreliable.
				// See:
				// http://android-developers.blogspot.com/2011/09/androids-http-clients.html
				stack = new HttpClientStack(AndroidHttpClient.newInstance(userAgent));
			}
		}
		Network network = new BasicNetwork(stack);
		RequestQueue queue;
		if (isOpenCache) {
			if (maxDiskCacheBytes <= -1) {
				// No maximum size specified
				queue = new RequestQueue(new DiskCache(getDiskCacheDir(context, DEFAULT_CACHE_DIR)), network);
			} else {
				// Disk cache size specified
				queue = new RequestQueue(new DiskCache(getDiskCacheDir(context, DEFAULT_CACHE_DIR), maxDiskCacheBytes), network);
			}
		} else {
			// No use Disk cache, usually on File download
			queue = new RequestQueue(null, network);
		}
		queue.start();
		return queue;
	}

	/**
	 * Creates a default instance of the worker pool and calls
	 * {@link RequestQueue#start()} on it. You may set a maximum size of the
	 * disk cache in bytes.
	 *
	 * @param context
	 *            A {@link Context} to use for creating the cache dir.
	 * @param maxDiskCacheBytes
	 *            the maximum size of the disk cache, in bytes. Use -1 for
	 *            default size.
	 * @return A started {@link RequestQueue} instance.
	 */
	public static RequestQueue newRequestQueue(Context context, int maxDiskCacheBytes) {
		return newRequestQueue(context, null, maxDiskCacheBytes, true);
	}

	/**
	 * Creates a default instance of the worker pool and calls
	 * {@link RequestQueue#start()} on it. You may set use or unused disk cache.
	 *
	 * @param context
	 *            A {@link Context} to use for creating the cache dir.
	 * @param isOpenCache
	 *            whether open disk cache.
	 * @return A started {@link RequestQueue} instance.
	 */
	public static RequestQueue newRequestQueue(Context context, boolean isOpenCache) {
		return newRequestQueue(context, null, -1, isOpenCache);
	}

	/**
	 * Creates a default instance of the worker pool and calls
	 * {@link RequestQueue#start()} on it.
	 *
	 * @param context
	 *            A {@link Context} to use for creating the cache dir.
	 * @param stack
	 *            An {@link HttpStack} to use for the network, or null for
	 *            default.
	 * @return A started {@link RequestQueue} instance.
	 */
	public static RequestQueue newRequestQueue(Context context, HttpStack stack) {
		return newRequestQueue(context, stack, -1, true);
	}

	/**
	 * Creates a default instance of the worker pool and calls
	 * {@link RequestQueue#start()} on it.
	 *
	 * @param context
	 *            A {@link Context} to use for creating the cache dir.
	 * @return A started {@link RequestQueue} instance.
	 */
	public static RequestQueue newRequestQueue(Context context) {
		return newRequestQueue(context, null);
	}
}
