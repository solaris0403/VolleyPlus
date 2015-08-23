package com.tony.volleydemo;

import java.io.File;

import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.tony.volleydemo.http.core.Listener;
import com.tony.volleydemo.http.core.Request.Method;
import com.tony.volleydemo.http.core.RequestQueue;
import com.tony.volleydemo.http.core.VolleyError;
import com.tony.volleydemo.http.request.FileDownloadRequest;
import com.tony.volleydemo.http.request.ImageRequest;
import com.tony.volleydemo.http.request.JsonObjectRequest;
import com.tony.volleydemo.http.request.JsonRequest;
import com.tony.volleydemo.http.request.StringRequest;
import com.tony.volleydemo.http.tool.FileDownloader;
import com.tony.volleydemo.http.tool.Volley;

public class MainActivity extends Activity {

	private ImageView mImgPic;
	private TextView mTxtContent;
	private static final String mSaveDirPath = "/sdcard/volleydownload/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mImgPic = (ImageView) findViewById(R.id.img_pic);
		mTxtContent = (TextView) findViewById(R.id.tv_content);
		final RequestQueue queue = Volley.newRequestQueue(this);
		FileDownloader mFileDownloader = new FileDownloader(queue, 1);

		File downloadDir = new File(mSaveDirPath);
		if (!downloadDir.exists())
			downloadDir.mkdir();
		mFileDownloader.add(mSaveDirPath + "l.jpg", "http://f.hiphotos.baidu.com/image/pic/item/b3b7d0a20cf431ad0e43d1584d36acaf2edd983a.jpg", new Listener<Void>() {

			@Override
			public void onSuccess(Void response) {
				// TODO Auto-generated method stub
				System.out.println(response.toString());
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
				mTxtContent.setText(downloadedSize +"/"+fileSize);
				System.out.println(downloadedSize+"");
			}
			@Override
			public void onUsedCache() {
				// TODO Auto-generated method stub
				super.onUsedCache();
			}
		});

		// final StringRequest request = new StringRequest(Method.GET,
		// "http://www.baidu.com", new Listener<String>() {
		//
		// @Override
		// public void onSuccess(String response) {
		// mTxtContent.setText(response);
		// }
		// });
		// queue.add(request);

		mImgPic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			//	queue.add(request);
			}
		});
	}
}
