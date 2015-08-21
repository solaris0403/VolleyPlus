package com.tony.volleydemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.tony.volleydemo.http.core.Listener;
import com.tony.volleydemo.http.core.Request.Method;
import com.tony.volleydemo.http.core.RequestQueue;
import com.tony.volleydemo.http.core.VolleyError;
import com.tony.volleydemo.http.request.StringRequest;
import com.tony.volleydemo.http.tool.Volley;

public class MainActivity extends Activity {

	private ImageView mImgPic;
	private TextView mTxtContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mImgPic = (ImageView) findViewById(R.id.img_pic);
		mTxtContent = (TextView) findViewById(R.id.tv_content);
		RequestQueue queue = Volley.newRequestQueue(this);
		StringRequest request = new StringRequest(Method.GET, "https://www.baidu.com", new Listener<String>() {
			@Override
			public void onSuccess(String response) {
				mTxtContent.setText(response);
			}
			@Override
			public void onError(VolleyError error) {
				super.onError(error);
			}
			@Override
			public void onFinish() {
				super.onFinish();
			}
			@Override
			public void onCancel() {
				super.onCancel();
			}
			@Override
			public void onNetworking() {
				super.onNetworking();
			}
			@Override
			public void onPreExecute() {
				super.onPreExecute();
			}
			@Override
			public void onProgressChange(long fileSize, long downloadedSize) {
				super.onProgressChange(fileSize, downloadedSize);
			}
			@Override
			public void onRetry() {
				super.onRetry();
			}
			@Override
			public void onUsedCache() {
				super.onUsedCache();
			}
		});
		queue.add(request);
	}
}
