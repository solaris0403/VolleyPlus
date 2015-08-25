package com.tony.volleydemo.http.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.tony.volleydemo.http.cache.LruCache;
import com.tony.volleydemo.http.core.Request;
import com.tony.volleydemo.http.core.RequestQueue;
import com.tony.volleydemo.http.image.ImageLoader;
import com.tony.volleydemo.http.tool.Volley;

/**
 * @author Tony E-mail:solaris0403@gmail.com
 * @version Create Data：Aug 25, 2015 4:01:55 PM
 */
public class VolleyPlus {
	private static Context mContext;
	private static RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;

	private VolleyPlus(Context context) {
	}

	/**
	 * 在Application里初始化
	 * 
	 * @param context
	 * @param config
	 */
	public static void init(Context context) {
		if (context == null) {
			throw new NullPointerException("context is null");
		}
		mContext = context;
		start();
	}

	private static void start() {
		if (mRequestQueue != null) {
			throw new IllegalStateException("RequestQueue has initialized");
		}
		mRequestQueue = getRequestQueue();
	}

	public static RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
				mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
		}
		return mRequestQueue;
	}

	public static <T> void addToRequestQueue(Request<T> req) {
		getRequestQueue().add(req);
	}

	public ImageLoader getImageLoader() {
		return mImageLoader;
	}

	// =========================开放的方法

	/**
	 * VolleyPlus setting class
	 * 
	 * @author user
	 *
	 */
	public static class VolleyPlusConfig {
		private int NETWORK_THREADPOOL_SIZE = 4;
		private String CACHE_FILEDIR_NAME = "volleyplus";
		private int CACHE_FILE_SIZE = 5 * 1024 * 1024;
		private boolean ISOPENCACHE = true;
	}
}
