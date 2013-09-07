package com.plantec.uwm;

import org.jsoup.nodes.Document;

import com.plantec.uwm.http.HttpManager;
import com.plantec.uwm.mail.MailHandler;

import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnItemClickListener {
	private SharedPreferences settings;
	private HttpManager mHttp;
	private MailHandler mHandler;
	private ListView mMailList;
	private TextView mEmptyList;
	private String mUsername;
	private String mPassword;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		settings = getSharedPreferences("UserInfo", 0);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		
		if (!settings.contains("Username")){
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
		} else {
			setup();
		}
	}
	
	private void setup(){
		mMailList = (ListView) findViewById(R.id.main_list);
		mEmptyList = (TextView) findViewById(R.id.main_empty_view);
		mHttp = HttpManager.getInstance();
		
		mUsername = settings.getString("Username", "").toString();
		mPassword = settings.getString("Password", "").toString();

		try {
			if (!mHttp.login(mUsername, mPassword)){
				finish();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			if (!mHttp.webmailLogin(mUsername, mPassword)){
				finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mHandler = new MailHandler();
		Document doc = mHandler.complete();
		mHandler.parse(doc);
		
		ListAdapter adapter = new ListAdapter(this, 
                R.layout.main_list_item, mHandler.getMail());
        
		mMailList.setAdapter(adapter);
		mMailList.setOnItemClickListener(this);
		
	//	SharedPreferences.Editor editor = settings.edit();
	//	editor.clear();
	//	editor.commit();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Log.i("CLICKER", Integer.toString(position) + " " + Long.toString(id));
		Intent intent = new Intent(this, ContentsActivity.class);
		intent.putExtra("url", mHandler.getMail().get(position).getUrl());
		startActivity(intent);
	}
}
