package com.plantec.uwm;

import com.plantec.uwm.server.BaseHTTPCommand;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends Activity {
	BaseHTTPCommand http = new BaseHTTPCommand();
	EditText mUsernameField;
	EditText mPasswordField;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		mUsernameField = (EditText) findViewById(R.id.netLinkText);
		mPasswordField = (EditText) findViewById(R.id.passwordText);
		
		//TODO ASYNC TASK
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
	}
	
	public void login(View view){
		try {
			if (!isNetworkAvailable())
				throw new Exception("Network not available");
			
			if (http.login(mUsernameField.getText().toString(), mPasswordField.getText().toString()) <= 1)
				throw new Exception("Wrong creds");
			
			Intent intent = new Intent(this, MainActivity.class);
			intent.putExtra("username", mUsernameField.getText().toString());
			intent.putExtra("password", mPasswordField.getText().toString());
			startActivity(intent);
		} catch (Exception e) {
			mUsernameField.setText("Wrong Creds");
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
