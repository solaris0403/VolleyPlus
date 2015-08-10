package com.tony.volleydemo.http.utils;

import android.content.Context;

import com.tony.volleydemo.http.core.RequestQueue;

/**
 * @author Tony E-mail:solaris0403@gmail.com
 * @version Create Data：Aug 10, 2015 9:24:06 AM
 */
public class Hera {
	private Hera() {
	}
	private static RequestQueue mRequestQueue;

	// 图片加载管理器，私有该实例，提供方法对外服务。
	// private static ImageLoader mImageLoader;

	// 文件下载管理器，私有该实例，提供方法对外服务。
	// private static FileDownloader mFileDownloader;
	
	public static void init(Context context){
		 if (mRequestQueue != null) throw new IllegalStateException("RequestQueue has initialized");
	}
}
