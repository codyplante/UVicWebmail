package com.plantec.uwm;

import com.plantec.uwm.server.BaseHTTPCommand;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.widget.TextView;

public class MainActivity extends Activity {
	BaseHTTPCommand http = new BaseHTTPCommand();
	TextView view;
//	ListView mMailList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TextView view = (TextView) findViewById(R.id.text);
//		mMailList = (ListView) findViewById(R.id.main_list);
		view.setText("hello");
		
		Intent intent = getIntent();
		String result = "";
		try {
			http.login(intent.getExtras().get("username").toString(), intent.getExtras().get("password").toString());
			result = http.webmailLogin(intent.getExtras().get("username").toString(), intent.getExtras().get("password").toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		view.setText(result);
	}
}
