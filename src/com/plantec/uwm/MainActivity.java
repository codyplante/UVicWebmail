package com.plantec.uwm;

import com.plantec.uwm.http.HttpManager;
import com.plantec.uwm.mail.MailHandler;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	private HttpManager mHttp;
	private TextView mText;
	private ListView mList;
	private String mUsername;
	private String mPassword;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		
		mText = (TextView) findViewById(R.id.text);
		mList = (ListView) findViewById(R.id.main_list);
		mHttp = HttpManager.getInstance();
		
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
		
		MailHandler mHandler = new MailHandler();
		
		
		ListAdapter adapter = new ListAdapter(this, 
                R.layout.main_list_item, mHandler.getMail());
        
        mList.setAdapter(adapter);
		
		mText.setText(Integer.toString(result));
	}
}
