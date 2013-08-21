package com.plantec.uwm;

import com.plantec.uwm.server.BaseHTTPCommand;

import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
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
			http.login(mUsernameField.getText().toString(), mPasswordField.getText().toString());
			Intent intent = new Intent(this, MainActivity.class);
			intent.putExtra("username", mUsernameField.getText().toString());
			intent.putExtra("password", mPasswordField.getText().toString());
			//startActivity(intent);
		} catch (Exception e) {
			mUsernameField.setText("");
			mPasswordField.setText("");
		}
	}
}
