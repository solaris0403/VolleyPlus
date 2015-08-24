package com.tony.volleydemo;

import java.io.File;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.tony.volleydemo.http.core.Listener;
import com.tony.volleydemo.http.core.Request.Method;
import com.tony.volleydemo.http.core.AuthFailureError;
import com.tony.volleydemo.http.core.RequestQueue;
import com.tony.volleydemo.http.core.VolleyError;
import com.tony.volleydemo.http.request.ClearCacheRequest;
import com.tony.volleydemo.http.request.FileDownloadRequest;
import com.tony.volleydemo.http.request.ImageRequest;
import com.tony.volleydemo.http.request.JsonArrayRequest;
import com.tony.volleydemo.http.request.JsonObjectRequest;
import com.tony.volleydemo.http.request.JsonRequest;
import com.tony.volleydemo.http.request.StringRequest;
import com.tony.volleydemo.http.stack.HttpClientStack;
import com.tony.volleydemo.http.stack.HttpStack;
import com.tony.volleydemo.http.tool.FileDownloader;
import com.tony.volleydemo.http.tool.Volley;

public class MainActivity extends Activity {
	private static final String TAG = "Volley Log";
	private ImageView mImgPic;
	private TextView mTxtContent;
	private Button mBtnStart, mBtnCLear;
	private String app_id = "6819";
	private String app_secret = "470e425009b0403583a5161cb4ca7985";
	private static final String mSaveDirPath = "/sdcard/volley/";
	FileDownloader mFileDownloader;
	RequestQueue queue;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		queue = Volley.newRequestQueue(this,false);
		mImgPic = (ImageView) findViewById(R.id.img_pic);
		mTxtContent = (TextView) findViewById(R.id.tv_content);
		mBtnStart = (Button) findViewById(R.id.btn_start);
		mBtnCLear = (Button) findViewById(R.id.btn_clear);
		mBtnStart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				testFileDownload(queue);
			}
		});
		mBtnCLear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				testClear(queue);
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
		String url = "http://route.showapi.com/341-1?showapi_appid="+app_id+"&showapi_timestamp="+"20150824101239"+"&showapi_sign="+app_secret+"&time=2015-08-24&page=1";
		StringRequest request = new StringRequest(Method.GET, url, new Listener<String>() {

			@Override
			public void onSuccess(String response) {
				Log.e(TAG, "onSuccess:"+response);
				//mTxtContent.setText(response);
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
	
	private void testJsonArray(RequestQueue queue){
		String url = "http://stage.klp.unileverfoodsolutions.tw/v1/pointcatalog/products?per_page=30&onsale=ture&access_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiIsImtpZCI6ImUxd2tjNThhOXIifQ.eyJ1aWQiOiI1NTliNzYxNWZkNjA0YWI3NzY4YjQ1NjciLCJzY29wZXMiOltdLCJhcHAiOiI1NTljOGU0MWZkNjA0YTlmMTk4YjQ1NjcifQ.NzccJTxQD_yIPSaA1kghiWGc_0PKdliD691nVaLHX-g";
		JsonArrayRequest request = new JsonArrayRequest(Method.GET, url, new Listener<JSONArray>() {
			@Override
			public void onSuccess(JSONArray response) {
				Log.e(TAG, "onSuccess:"+response.toString());
				//mTxtContent.setText(response);
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
	
	private void testJsonObject(RequestQueue queue){
		String url = "http://route.showapi.com/341-1?showapi_appid="+app_id+"&showapi_timestamp="+"20150824102539"+"&showapi_sign="+app_secret+"&time=2015-08-24&page=1";
		String urlB = "http://stage.klp.unileverfoodsolutions.tw/v1/pointcatalog/products?per_page=30&onsale=ture&access_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiIsImtpZCI6ImUxd2tjNThhOXIifQ.eyJ1aWQiOiI1NTliNzYxNWZkNjA0YWI3NzY4YjQ1NjciLCJzY29wZXMiOltdLCJhcHAiOiI1NTljOGU0MWZkNjA0YTlmMTk4YjQ1NjcifQ.NzccJTxQD_yIPSaA1kghiWGc_0PKdliD691nVaLHX-g";

		JsonObjectRequest request = new JsonObjectRequest(Method.GET, urlB, null, new Listener<JSONObject>() {

			@Override
			public void onSuccess(JSONObject response) {
				Log.e(TAG, "onSuccess:"+response.toString());
				//mTxtContent.setText(response);
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
	
	private void testBitmap(RequestQueue queue){
		String url = "https://dn-appcms.qbox.me/1440152508690.png";
		ImageRequest request = new ImageRequest(url, new Listener<Bitmap>() {
			@Override
			public void onSuccess(Bitmap response) {
				Log.e(TAG, "onSuccess:"+response.getByteCount());
				//mTxtContent.setText(response);
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
	
	private void testClear(RequestQueue queue){
		ClearCacheRequest request = new ClearCacheRequest(queue.getCache(), null, new Listener<Object>() {

			@Override
			public void onSuccess(Object response) {
				Log.e(TAG, "onSuccess:"+response.hashCode());
				//mTxtContent.setText(response);
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
	
	@Override
	public void finish() {
		if (mFileDownloader!=null) {
			mFileDownloader.clearAll();
		}
		if (queue!=null) {
			queue.stop();
		}
		super.finish();
	}
}
