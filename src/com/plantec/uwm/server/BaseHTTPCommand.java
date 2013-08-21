package com.plantec.uwm.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class BaseHTTPCommand {
	private CookieStore mCookieStore = new BasicCookieStore();
	private HttpContext mLocalContext = new BasicHttpContext();
	private DefaultHttpClient mHttpClient = new DefaultHttpClient();
	private HttpResponse mResponse;
	private HttpGet mHttpGet;
	private HttpPost mHttpPost;
	private List <NameValuePair> nvps;
	
	public BaseHTTPCommand(){
		mLocalContext.setAttribute(ClientContext.COOKIE_STORE, mCookieStore);
		mHttpClient.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
	}

	public void login(String username, String password) throws Exception{
		mResponse = httpGet("https://www.uvic.ca/cas/login?service=http%3A%2F%2Fwww.uvic.ca%2F");
		consumeEntity(mResponse);
		
		nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("username", username));
		nvps.add(new BasicNameValuePair("password", password));
		nvps.add(new BasicNameValuePair("lt", "e1s1"));
		nvps.add(new BasicNameValuePair("_eventId", "submit"));
		mResponse = httpPost("https://www.uvic.ca/cas/login?service=http%3A%2F%2Fdev.uvic.ca%2Fcurrent-students%2Findex.php", nvps);
		consumeEntity(mResponse);
	}
	
	public void consumeEntity(HttpResponse response) throws Exception{
		//TODO Testing
		System.out.println("Login form get: " + response.getStatusLine());
		HttpEntity entity = response.getEntity();
		if (entity != null) 
		    entity.consumeContent();
	}
	
	public String webmailLogin(String username, String password) throws Exception{
		nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("login_username", username));
		nvps.add(new BasicNameValuePair("secretkey", password));
		mResponse = httpPost("https://wm3.uvic.ca/src/CASlogin.php", nvps);
		consumeEntity(mResponse);
		
		mResponse = httpPost("https://wm3.uvic.ca/src/redirect.php", nvps);
		consumeEntity(mResponse);
		
		mResponse = httpGet("https://wm3.uvic.ca/src/right_main.php");
		return EntityUtils.toString(mResponse.getEntity());	
	}
	
	public HttpResponse httpGet(String url) throws IOException{
		mHttpGet = new HttpGet(url);
		return mHttpClient.execute(mHttpGet, mLocalContext);
	}
	
	public HttpResponse httpPost(String url, List<NameValuePair> credentials) throws Exception{
		mHttpPost = new HttpPost(url);
		mHttpPost.setEntity(new UrlEncodedFormEntity(credentials, HTTP.UTF_8));
		return mHttpClient.execute(mHttpPost, mLocalContext);
	}
}
