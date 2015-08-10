package com.tony.volleydemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.tony.volleydemo.http.core.Request.Method;
import com.tony.volleydemo.http.core.RequestQueue;
import com.tony.volleydemo.http.core.Response.ErrorListener;
import com.tony.volleydemo.http.core.Response.Listener;
import com.tony.volleydemo.http.core.VolleyError;
import com.tony.volleydemo.http.tool.ImageRequest;
import com.tony.volleydemo.http.tool.StringRequest;
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
			public void onResponse(String response) {
				Log.e("123", response);
				mTxtContent.setText(response);
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("123", error.toString());
			}
		});

        ImageRequest imageRequest2 = new ImageRequest("http://pica.nipic.com/2007-11-09/2007119124413448_2.jpg", new Listener<Bitmap>() {

			@Override
			public void onResponse(Bitmap response) {
				mImgPic.setImageBitmap(response);
			}
		}, 0, 0,ScaleType.CENTER_CROP,Config.ARGB_8888, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				
			}
		});
        queue.add(request);
        queue.add(imageRequest2);
    }
}
