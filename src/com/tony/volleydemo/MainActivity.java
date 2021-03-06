package com.tony.volleydemo;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.tony.volleydemo.http.cache.BitmapImageCache;
import com.tony.volleydemo.http.core.Listener;
import com.tony.volleydemo.http.core.Request.Method;
import com.tony.volleydemo.http.core.RequestQueue;
import com.tony.volleydemo.http.core.VolleyError;
import com.tony.volleydemo.http.image.ImageLoader;
import com.tony.volleydemo.http.image.SelfImageLoader;
import com.tony.volleydemo.http.request.ClearCacheRequest;
import com.tony.volleydemo.http.request.ImageRequest;
import com.tony.volleydemo.http.request.JsonArrayRequest;
import com.tony.volleydemo.http.request.JsonObjectRequest;
import com.tony.volleydemo.http.request.StringRequest;
import com.tony.volleydemo.http.tool.FileDownloader;
import com.tony.volleydemo.http.tool.NetworkImageView;
import com.tony.volleydemo.http.tool.Volley;
import com.tony.volleydemo.http.utils.VolleyPlus;

public class MainActivity extends Activity {
	private NetworkImageView mNetworkImageView;
	private ImageLoader mImageLoader;
	private RequestQueue mQueue;
	private static final String TAG = "Volley Log";
	private ImageView mImgPic;
	private TextView mTxtContent;
	private Button mBtnStart, mBtnCLear;
	private String app_id = "6819";
	private String app_secret = "470e425009b0403583a5161cb4ca7985";
	private static final String mSaveDirPath = "/sdcard/volley/";
	FileDownloader mFileDownloader;
	String[] urls = new String[200];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		queue = Volley.newRequestQueue(this, false);
//
//		int memoryCacheSize = 5 * 1024 * 1024; // 5MB
//
//		File diskCacheDir = new File(getCacheDir(), "netroid");
//		int diskCacheSize = 50 * 1024 * 1024; // 50MB

//		mQueue = Volley.newRequestQueue(getApplicationContext(), diskCacheSize);
//		mImageLoader = new SelfImageLoader(mQueue, new BitmapImageCache(), getResources(), getAssets());

//		内存加载
//		int memoryCacheSize = 5 * 1024 * 1024; // 5MB
//		mQueue = Netroid.newRequestQueue(getApplicationContext(), null);
//
//		mImageLoader = new SelfImageLoader(mQueue, new BitmapImageCache(memoryCacheSize), getResources(), getAssets()) {
//			@Override
//			public void makeRequest(ImageRequest request) {
//				request.setCacheExpireTime(TimeUnit.DAYS, 10);
//			}
//		};
		//磁盘加载
//		File diskCacheDir = new File(getCacheDir(), "netroid");
//		int diskCacheSize = 50 * 1024 * 1024; // 50MB
//		mQueue = Netroid.newRequestQueue(getApplicationContext(), new DiskCache(diskCacheDir, diskCacheSize));
//
//		mImageLoader = new SelfImageLoader(mQueue, null, getResources(), getAssets()) {
//			@Override
//			public void makeRequest(ImageRequest request) {
//				request.setCacheExpireTime(TimeUnit.MINUTES, 10);
//			}
//		};
		//双加载
//		int memoryCacheSize = 5 * 1024 * 1024; // 5MB
//		File diskCacheDir = new File(getCacheDir(), "netroid");
//		int diskCacheSize = 50 * 1024 * 1024; // 50MB
//		mQueue = Netroid.newRequestQueue(getApplicationContext(), new DiskCache(diskCacheDir, diskCacheSize));
//		mImageLoader = new SelfImageLoader(mQueue, new BitmapImageCache(memoryCacheSize), getResources(), getAssets()) {
//			@Override
//			public void makeRequest(ImageRequest request) {
//				request.setCacheExpireTime(TimeUnit.MINUTES, 1);
//			}
//		};

		
		testListView();
		
		
		
		mImgPic = (ImageView) findViewById(R.id.img_pic);
		mNetworkImageView = (NetworkImageView) findViewById(R.id.img_net_pic);
		mTxtContent = (TextView) findViewById(R.id.tv_content);
		mBtnStart = (Button) findViewById(R.id.btn_start);
		mBtnCLear = (Button) findViewById(R.id.btn_clear);
		mBtnStart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				load();
//				testImageView();
				// testFileDownload(queue);
//				testHttpImageView();
//				loadAssetsImage();
//				loadSdcardImage();
//				testJsonArray();
			}
		});
		mBtnCLear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// testClear(queue);
			}
		});
		
		
		
	}

	private void testFileDownload(final RequestQueue queue) {
		FileDownloader mFileDownloader = new FileDownloader(queue, 1);
		File downloadDir = new File(mSaveDirPath);
		if (!downloadDir.exists())
			downloadDir.mkdir();
		mFileDownloader.add(mSaveDirPath + "j.apk", "http://download.game.yy.com/duowanapp/m/Duowan20140427.apk", new Listener<Void>() {

			@Override
			public void onSuccess(Void response) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
			}

			@Override
			public void onError(VolleyError error) {
				// TODO Auto-generated method stub
				super.onError(error);
			}

			@Override
			public void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
			}

			@Override
			public void onNetworking() {
				// TODO Auto-generated method stub
				super.onNetworking();
			}

			@Override
			public void onProgressChange(long fileSize, long downloadedSize) {
				// TODO Auto-generated method stub
				super.onProgressChange(fileSize, downloadedSize);
				mTxtContent.setText(downloadedSize + "/" + fileSize);
			}

			@Override
			public void onUsedCache() {
				// TODO Auto-generated method stub
				super.onUsedCache();
			}
		});
	}

	private void testString(RequestQueue queue) {
		String url = "http://route.showapi.com/341-1?showapi_appid=" + app_id + "&showapi_timestamp=" + "20150824101239" + "&showapi_sign=" + app_secret + "&time=2015-08-24&page=1";
		StringRequest request = new StringRequest(Method.GET, url, new Listener<String>() {

			@Override
			public void onSuccess(String response) {
				Log.e(TAG, "onSuccess:" + response);
				// mTxtContent.setText(response);
			}

			@Override
			public void onCancel() {
				Log.e(TAG, "onCancel");
				super.onCancel();
			}

			@Override
			public void onError(VolleyError error) {
				Log.e(TAG, "error:" + error);
				super.onError(error);
			}

			@Override
			public void onFinish() {
				Log.e(TAG, "onFinish");
				super.onFinish();
			}

			@Override
			public void onNetworking() {
				Log.e(TAG, "onNetworking");
				super.onNetworking();
			}

			@Override
			public void onPreExecute() {
				Log.e(TAG, "onPreExecute");
				super.onPreExecute();
			}

			@Override
			public void onProgressChange(long fileSize, long downloadedSize) {
				Log.e(TAG, "onProgressChange:fileSize=" + fileSize + " downloadedSize=" + downloadedSize);
				super.onProgressChange(fileSize, downloadedSize);
			}

			@Override
			public void onRetry() {
				Log.e(TAG, "onRetry");
				super.onRetry();
			}

			@Override
			public void onUsedCache() {
				Log.e(TAG, "onUsedCache");
				super.onUsedCache();
			}
		});
		queue.add(request);
	}

	private void testJsonArray() {
		String url = "http://api.androidhive.info/volley/person_array.json";
		JsonArrayRequest request = new JsonArrayRequest(Method.GET, url, new Listener<JSONArray>() {
			@Override
			public void onSuccess(JSONArray response) {
				Log.e(TAG, "onSuccess:" + response.toString());
				 mTxtContent.setText(response.toString());
			}

			@Override
			public void onCancel() {
				Log.e(TAG, "onCancel");
				super.onCancel();
			}

			@Override
			public void onError(VolleyError error) {
				Log.e(TAG, "error:" + error);
				super.onError(error);
			}

			@Override
			public void onFinish() {
				Log.e(TAG, "onFinish");
				super.onFinish();
			}

			@Override
			public void onNetworking() {
				Log.e(TAG, "onNetworking");
				super.onNetworking();
			}

			@Override
			public void onPreExecute() {
				Log.e(TAG, "onPreExecute");
				super.onPreExecute();
			}

			@Override
			public void onProgressChange(long fileSize, long downloadedSize) {
				Log.e(TAG, "onProgressChange:fileSize=" + fileSize + " downloadedSize=" + downloadedSize);
				super.onProgressChange(fileSize, downloadedSize);
			}

			@Override
			public void onRetry() {
				Log.e(TAG, "onRetry");
				super.onRetry();
			}

			@Override
			public void onUsedCache() {
				Log.e(TAG, "onUsedCache");
				super.onUsedCache();
			}
		});
		VolleyPlus.addToRequestQueue(request);
	}

	private void testJsonObject(RequestQueue queue) {
		String url = "http://route.showapi.com/341-1?showapi_appid=" + app_id + "&showapi_timestamp=" + "20150824102539" + "&showapi_sign=" + app_secret + "&time=2015-08-24&page=1";
		String urlB = "http://stage.klp.unileverfoodsolutions.tw/v1/pointcatalog/products?per_page=30&onsale=ture&access_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiIsImtpZCI6ImUxd2tjNThhOXIifQ.eyJ1aWQiOiI1NTliNzYxNWZkNjA0YWI3NzY4YjQ1NjciLCJzY29wZXMiOltdLCJhcHAiOiI1NTljOGU0MWZkNjA0YTlmMTk4YjQ1NjcifQ.NzccJTxQD_yIPSaA1kghiWGc_0PKdliD691nVaLHX-g";

		JsonObjectRequest request = new JsonObjectRequest(Method.GET, urlB, null, new Listener<JSONObject>() {

			@Override
			public void onSuccess(JSONObject response) {
				Log.e(TAG, "onSuccess:" + response.toString());
				// mTxtContent.setText(response);
			}

			@Override
			public void onCancel() {
				Log.e(TAG, "onCancel");
				super.onCancel();
			}

			@Override
			public void onError(VolleyError error) {
				Log.e(TAG, "error:" + error);
				super.onError(error);
			}

			@Override
			public void onFinish() {
				Log.e(TAG, "onFinish");
				super.onFinish();
			}

			@Override
			public void onNetworking() {
				Log.e(TAG, "onNetworking");
				super.onNetworking();
			}

			@Override
			public void onPreExecute() {
				Log.e(TAG, "onPreExecute");
				super.onPreExecute();
			}

			@Override
			public void onProgressChange(long fileSize, long downloadedSize) {
				Log.e(TAG, "onProgressChange:fileSize=" + fileSize + " downloadedSize=" + downloadedSize);
				super.onProgressChange(fileSize, downloadedSize);
			}

			@Override
			public void onRetry() {
				Log.e(TAG, "onRetry");
				super.onRetry();
			}

			@Override
			public void onUsedCache() {
				Log.e(TAG, "onUsedCache");
				super.onUsedCache();
			}
		});
		queue.add(request);
	};

	private void testBitmap(RequestQueue queue) {
		String url = "https://dn-appcms.qbox.me/1440152508690.png";
		ImageRequest request = new ImageRequest(url, new Listener<Bitmap>() {
			@Override
			public void onSuccess(Bitmap response) {
				Log.e(TAG, "onSuccess:" + response.getByteCount());
				// mTxtContent.setText(response);
				mImgPic.setImageBitmap(response);
			}

			@Override
			public void onCancel() {
				Log.e(TAG, "onCancel");
				super.onCancel();
			}

			@Override
			public void onError(VolleyError error) {
				Log.e(TAG, "error:" + error);
				super.onError(error);
			}

			@Override
			public void onFinish() {
				Log.e(TAG, "onFinish");
				super.onFinish();
			}

			@Override
			public void onNetworking() {
				Log.e(TAG, "onNetworking");
				super.onNetworking();
			}

			@Override
			public void onPreExecute() {
				Log.e(TAG, "onPreExecute");
				super.onPreExecute();
			}

			@Override
			public void onProgressChange(long fileSize, long downloadedSize) {
				Log.e(TAG, "onProgressChange:fileSize=" + fileSize + " downloadedSize=" + downloadedSize);
				super.onProgressChange(fileSize, downloadedSize);
			}

			@Override
			public void onRetry() {
				Log.e(TAG, "onRetry");
				super.onRetry();
			}

			@Override
			public void onUsedCache() {
				Log.e(TAG, "onUsedCache");
				super.onUsedCache();
			}
		}, 0, 0, ScaleType.CENTER_CROP, Config.RGB_565);
		queue.add(request);
	}

	private void testClear(RequestQueue queue) {
		ClearCacheRequest request = new ClearCacheRequest(queue.getCache(), null, new Listener<Object>() {

			@Override
			public void onSuccess(Object response) {
				Log.e(TAG, "onSuccess:" + response.hashCode());
				// mTxtContent.setText(response);
			}

			@Override
			public void onCancel() {
				Log.e(TAG, "onCancel");
				super.onCancel();
			}

			@Override
			public void onError(VolleyError error) {
				Log.e(TAG, "error:" + error);
				super.onError(error);
			}

			@Override
			public void onFinish() {
				Log.e(TAG, "onFinish");
				super.onFinish();
			}

			@Override
			public void onNetworking() {
				Log.e(TAG, "onNetworking");
				super.onNetworking();
			}

			@Override
			public void onPreExecute() {
				Log.e(TAG, "onPreExecute");
				super.onPreExecute();
			}

			@Override
			public void onProgressChange(long fileSize, long downloadedSize) {
				Log.e(TAG, "onProgressChange:fileSize=" + fileSize + " downloadedSize=" + downloadedSize);
				super.onProgressChange(fileSize, downloadedSize);
			}

			@Override
			public void onRetry() {
				Log.e(TAG, "onRetry");
				super.onRetry();
			}

			@Override
			public void onUsedCache() {
				Log.e(TAG, "onUsedCache");
				super.onUsedCache();
			}
		});
		queue.add(request);
	}

	private void testImageView() {
		String url = "http://upload.newhua.com/3/3e/1292303714308.jpg";
//		ImageLoader.ImageListener listener = ImageLoader.getImageListener(mImgPic, android.R.drawable.ic_menu_rotate, android.R.drawable.ic_delete);
//		mImageLoader.get(url, listener);
		VolleyPlus.load(mImgPic, url, 0, 0);
	}
	
	private void testHttpImageView(){
		String url = "http://upload.newhua.com/3/3e/1292303714308.jpg";
		mNetworkImageView.setImageUrl(url, VolleyPlus.getImageLoader());
	}
	private void loadAssetsImage() {
		mNetworkImageView.setImageUrl(SelfImageLoader.RES_ASSETS + "cover_16539.jpg", mImageLoader);
	}
	private void loadSdcardImage() {
		mNetworkImageView.setImageUrl(SelfImageLoader.RES_SDCARD + "/sdcard/knorr/picture/1.png", mImageLoader);
	}

	@Override
	public void finish() {
		if (mFileDownloader != null) {
			mFileDownloader.clearAll();
		}
		super.finish();
	}
	
	
	
	private void testListView(){
		for (int i = 0; i < 200; i++) {
			if (0==i%2) {
				urls[i] = "http://upload.newhua.com/3/3e/1292303714308.jpg";
			}else {
				urls[i] = "http://img05.tooopen.com/images/20150729/tooopen_sy_135912171182.jpg";
			}
		}
	}
	
	private void load(){
		ListView listView = (ListView) findViewById(R.id.listview);
		listView.setAdapter(new MyAdapter(MainActivity.this, urls));
	}
	
	
	
	
	class MyAdapter extends BaseAdapter{
		Context mContext;
		String[] urlStrings;
		public MyAdapter(Context context, String[] url) {
			mContext = context;
			urlStrings = url;
		}
		@Override
		public int getCount() {
			return urlStrings.length;
		}

		@Override
		public Object getItem(int position) {
			return urlStrings[position];
		}

		@Override
		public long getItemId(int position) {
			return urlStrings[position].hashCode();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(R.layout.item_view, null);
				holder.mView = (ImageView) convertView.findViewById(R.id.img_view);
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}
			VolleyPlus.load(holder.mView, urlStrings[position], 0, 0);
			return convertView;
		}
		
		class ViewHolder{
			ImageView mView;
		}
	}
}
