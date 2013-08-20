package com.plantec.uwm;

import java.io.IOException;

import com.plantec.uwm.server.BaseHTTPCommand;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends Activity {
	EditText mUsernameField = (EditText) findViewById(R.id.netLinkText);
	EditText mPasswordField = (EditText) findViewById(R.id.passwordText);
	BaseHTTPCommand http = new BaseHTTPCommand();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}
	
	public void login(View view){
		//	http.login(mUsernameField.toString(), mPasswordField.toString());
			Intent intent = new Intent(this, MainActivity.class);
			intent.putExtra("username", mUsernameField.toString());
			intent.putExtra("password", mPasswordField.toString());
			startActivity(intent);
	}
	
}
