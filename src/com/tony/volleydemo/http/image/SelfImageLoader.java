package com.tony.volleydemo.http.image;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.widget.ImageView.ScaleType;

import com.tony.volleydemo.http.core.NetworkResponse;
import com.tony.volleydemo.http.core.RequestQueue;
import com.tony.volleydemo.http.request.ImageRequest;

public class SelfImageLoader extends ImageLoader {

	public static final String RES_ASSETS = "assets://";
	public static final String RES_SDCARD = "sdcard://";
	public static final String RES_DRAWABLE = "drawable://";
	public static final String RES_HTTP = "http://";

	private AssetManager mAssetManager;
	private Resources mResources;

	public SelfImageLoader(RequestQueue queue, ImageCache cache, Resources resources, AssetManager assetManager) {
		super(queue, cache);
		mResources = resources;
		mAssetManager = assetManager;
	}

	public void makeRequest(ImageRequest request) {
		// request.setCacheExpireTime(TimeUnit.MINUTES, 10);
	}

	public static byte[] toBytes(InputStream is) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		int nRead;
		byte[] data = new byte[16384];
		while ((nRead = is.read(data, 0, data.length)) != -1) {
			buffer.write(data, 0, nRead);
		}
		buffer.flush();

		return buffer.toByteArray();
	}

	public static byte[] bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	@Override
	public ImageRequest buildRequest(String requestUrl, int maxWidth, int maxHeight, ScaleType scaleType, Config config) {
		ImageRequest request;
		if (requestUrl.startsWith(RES_ASSETS)) {
			request = new ImageRequest(requestUrl.substring(RES_ASSETS.length()), maxWidth, maxHeight) {
				@Override
				public NetworkResponse perform() {
					try {
						return new NetworkResponse(toBytes(mAssetManager.open(getUrl())));
					} catch (IOException e) {
						return new NetworkResponse(new byte[1]);
					}
				}
			};
		} else if (requestUrl.startsWith(RES_SDCARD)) {
			request = new ImageRequest(requestUrl.substring(RES_SDCARD.length()), maxWidth, maxHeight) {
				@Override
				public NetworkResponse perform() {
					try {
						return new NetworkResponse(toBytes(new FileInputStream(getUrl())));
					} catch (IOException e) {
						return new NetworkResponse(new byte[1]);
					}
				}
			};
		} else if (requestUrl.startsWith(RES_DRAWABLE)) {
			request = new ImageRequest(requestUrl.substring(RES_DRAWABLE.length()), maxWidth, maxHeight) {
				@Override
				public NetworkResponse perform() {
					try {
						int resId = Integer.parseInt(getUrl());
						Bitmap bitmap = BitmapFactory.decodeResource(mResources, resId);
						return new NetworkResponse(bitmap2Bytes(bitmap));
					} catch (Exception e) {
						return new NetworkResponse(new byte[1]);
					}
				}
			};
		} else if (requestUrl.startsWith(RES_HTTP)) {
			request = new ImageRequest(requestUrl, maxWidth, maxHeight);
		} else {
			return null;
		}

		makeRequest(request);
		return request;
	}

}
