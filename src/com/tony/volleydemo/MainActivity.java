package com.tony.volleydemo;

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
import com.tony.volleydemo.http.request.StringRequest;
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
		final StringRequest request = new StringRequest(Method.GET, "https://www.baidu.com", new Listener<String>() {
			@Override
			public void onSuccess(String response) {
				mTxtContent.setText(response);
			}
			@Override
			public void onError(VolleyError error) {
				super.onError(error);
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
