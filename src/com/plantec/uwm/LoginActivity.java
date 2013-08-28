package com.plantec.uwm;

import com.plantec.uwm.http.HttpManager;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends Activity {
	private HttpManager mHttp;
	private EditText mUsernameField;
	private EditText mPasswordField;
	private String mUsername;
	private String mPassword;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
	
		mUsernameField = (EditText) findViewById(R.id.netLinkText);
		mPasswordField = (EditText) findViewById(R.id.passwordText);
		mHttp = HttpManager.getInstance();
		
		//TODO ASYNC TASK
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
	}
	
	public void login(View view){
		mUsername = mUsernameField.getText().toString();
		mPassword = mPasswordField.getText().toString();
		
		try {
			if (!isNetworkAvailable()){
				mUsernameField.setText("Network Bro");
				throw new Exception("Network not available");
			}
			if (mHttp.login(mUsername, mPassword) <= 1){
				mUsernameField.setText("Wrong Creds Bro");
				throw new Exception("Wrong creds");
			}
			
			SharedPreferences settings = getSharedPreferences("UserInfo", 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("Username", mUsername);
			editor.putString("Password", mPassword);
			editor.commit();
			
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		} catch (Exception e) {
			//mUsernameField.setText("Wrong Creds");
			mPasswordField.setText("");
			//The credentials you entered do not match our records. Try again.
			e.printStackTrace();
		}
	}
	
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null 
	    		&& activeNetworkInfo.isConnectedOrConnecting() 
	    		&& connectivityManager.getActiveNetworkInfo().isAvailable() 
	    		&& connectivityManager.getActiveNetworkInfo().isConnected();
	}
}
