package com.tony.volleydemo;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tony.volleydemo.http.core.Listener;
import com.tony.volleydemo.http.core.Request.Method;
import com.tony.volleydemo.http.core.RequestQueue;
import com.tony.volleydemo.http.core.VolleyError;
import com.tony.volleydemo.http.request.FileDownloadRequest;
import com.tony.volleydemo.http.request.JsonObjectRequest;
import com.tony.volleydemo.http.request.JsonRequest;
import com.tony.volleydemo.http.request.StringRequest;
import com.tony.volleydemo.http.tool.FileDownloader;
import com.tony.volleydemo.http.tool.Volley;

public class MainActivity extends Activity {

	private Button mImgPic;
	private TextView mTxtContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mImgPic = (Button) findViewById(R.id.img_pic);
		mTxtContent = (TextView) findViewById(R.id.tv_content);
		final RequestQueue queue = Volley.newRequestQueue(this);
//		final StringRequest request = new StringRequest(Method.GET, "http://route.showapi.com/341-1", new Listener<String>() {
//			@Override
//			public void onSuccess(String response) {
//				mTxtContent.setText(response);
//			}
//			@Override
//			public void onError(VolleyError error) {
//				super.onError(error);
//			}
//		});
//		FileDownloader mFileDownloader = new FileDownloader(queue, 1);
//		mFileDownloader.add("/sdcard/netroid/down.file", "http://d.hiphotos.baidu.com/image/pic/item/3801213fb80e7bec3ff739452a2eb9389b506b60.jpg", new Listener<Void>() {
//
//			@Override
//			public void onSuccess(Void response) {
//				// TODO Auto-generated method stub
//				System.out.println(response.toString());
//			}
//			@Override
//			public void onError(VolleyError error) {
//				// TODO Auto-generated method stub
//				super.onError(error);
//			}
//			@Override
//			public void onPreExecute() {
//				// TODO Auto-generated method stub
//				super.onPreExecute();
//			}
//			@Override
//			public void onNetworking() {
//				// TODO Auto-generated method stub
//				super.onNetworking();
//			}
//			@Override
//			public void onProgressChange(long fileSize, long downloadedSize) {
//				// TODO Auto-generated method stub
//				super.onProgressChange(fileSize, downloadedSize);
//				
//			}
//		});
		final JsonObjectRequest request = new JsonObjectRequest("http://route.showapi.com/341-1", null, new Listener<JSONObject>() {

			@Override
			public void onSuccess(JSONObject response) {
				// TODO Auto-generated method stub
				mTxtContent.setText(response.toString());
			}
			@Override
			public void onError(VolleyError error) {
				// TODO Auto-generated method stub
				super.onError(error);
			}
			@Override
			public void onNetworking() {
				// TODO Auto-generated method stub
				super.onNetworking();
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
			}
			@Override
			public void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
			}
			@Override
			public void onProgressChange(long fileSize, long downloadedSize) {
				// TODO Auto-generated method stub
				super.onProgressChange(fileSize, downloadedSize);
			}
			@Override
			public void onUsedCache() {
				// TODO Auto-generated method stub
				super.onUsedCache();
			}
		});
		queue.add(request);
		
		mImgPic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				queue.add(request);
			}
		});
	}
}
