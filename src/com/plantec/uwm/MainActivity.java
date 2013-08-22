package com.plantec.uwm;

import com.plantec.uwm.http.HttpManager;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.widget.TextView;

public class MainActivity extends Activity {
	private HttpManager mHttp;
	private TextView view;
//	private ListView mMailList;
	private String mUsername;
	private String mPassword;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		view = (TextView) findViewById(R.id.text);
//		mMailList = (ListView) findViewById(R.id.main_list);
		mHttp = HttpManager.getInstance();
		
		view.setText("hello");
		
		Intent intent = getIntent();
		mUsername = intent.getExtras().get("username").toString();
		mPassword = intent.getExtras().get("password").toString();
		
		int result = 0;
		try {
			result = mHttp.webmailLogin(mUsername, mPassword);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		view.setText(Integer.toString(result));
	}
}
