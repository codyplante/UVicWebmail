package com.plantec.uwm;

import com.plantec.uwm.parser.HTMLParser;

import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.webkit.WebView;

public class ContentsActivity extends Activity {

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contents_activity);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		
		
		WebView webview = (WebView) findViewById(R.id.article_webview);
		//webview.setInitialScale(1);
		
		Intent intent = getIntent();
		String url = intent.getStringExtra("url");
		HTMLParser parse = new HTMLParser();
		
		String html = "<!DOCTYPE HTML><HTML><HEAD></HEAD><BODY>"
					+ parse.parseMailContent(url) + "</BODY></HTML>";
		webview.loadData(html, "text/html", "utf-8");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contents, menu);
		return true;
	}

}
