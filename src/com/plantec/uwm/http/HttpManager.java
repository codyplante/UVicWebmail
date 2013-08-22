package com.plantec.uwm.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

public class HttpManager {
	private static final String URL_LOGIN = "https://www.uvic.ca/cas/login?service=http%3A%2F%2Fwww.uvic.ca%2F";
	private static final String URL_WLOGIN = "https://wm3.uvic.ca/src/CASlogin.php"; 
	private static final String URL_REDIRECT = "https://wm3.uvic.ca/src/redirect.php";
	private static final String URL_CONTENT = "https://wm3.uvic.ca/src/right_main.php";
	private static final String URL_FOLDERS = "https://wm3.uvic.ca/src/left_main.php";
	
	private static HttpManager mManager = null;
	private static DefaultHttpClient mHttpClient = null;
	
	private CookieStore mCookieStore;
	private HttpContext mLocalContext;
	private HttpResponse mResponse;
	private HttpGet mHttpGet;
	private HttpPost mHttpPost;
	private List <NameValuePair> nvps;
	
	private HttpManager(){
		mHttpClient = new DefaultHttpClient();
		mCookieStore = new BasicCookieStore();
		mLocalContext = new BasicHttpContext();
		mHttpClient = new DefaultHttpClient();
		mLocalContext.setAttribute(ClientContext.COOKIE_STORE, mCookieStore);
		mHttpClient.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
	}
	
	public static HttpManager getInstance(){
		if (mManager == null)
			mManager = new HttpManager();
		
		return mManager;
	}
	
	public int login(String username, String password) throws Exception{
		mResponse = httpGet(URL_LOGIN);
		consumeEntity(mResponse);
		
		nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("username", username));
		nvps.add(new BasicNameValuePair("password", password));
		nvps.add(new BasicNameValuePair("lt", "e1s1"));
		nvps.add(new BasicNameValuePair("_eventId", "submit"));
		mResponse = httpPost(URL_LOGIN, nvps);
		consumeEntity(mResponse);
		
		return getCookieCount();
	}
	
	public void consumeEntity(HttpResponse response) throws Exception{
		//TODO Testing
		System.out.println("Login form get: " + response.getStatusLine());
		HttpEntity entity = response.getEntity();
		if (entity != null) 
		    entity.consumeContent();
	}
	
	public int webmailLogin(String username, String password) throws Exception{
		nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("login_username", username));
		nvps.add(new BasicNameValuePair("secretkey", password));
		mResponse = httpPost(URL_WLOGIN, nvps);
		consumeEntity(mResponse);
		
		mResponse = httpPost(URL_REDIRECT, nvps);
		consumeEntity(mResponse);
		
		mResponse = httpGet(URL_CONTENT);
		return getCookieCount();	
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
	
	public int getCookieCount(){
		List<Cookie> cookies = mCookieStore.getCookies();
		return cookies.size();
 	}
}
