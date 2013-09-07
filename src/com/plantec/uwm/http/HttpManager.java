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
import org.apache.http.util.EntityUtils;

public class HttpManager {
	private static final String URL_BASE = "https://wm3.uvic.ca/src/";
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
	
	public boolean login(String username, String password) throws Exception{
		mResponse = httpGet(URL_LOGIN);
		consumeEntity(mResponse);			

		List <NameValuePair> mCredentials = new ArrayList <NameValuePair>();
		mCredentials.add(new BasicNameValuePair("username", username));
		mCredentials.add(new BasicNameValuePair("password", password));
		mCredentials.add(new BasicNameValuePair("lt", "e1s1"));
		mCredentials.add(new BasicNameValuePair("_eventId", "submit"));
		mResponse = httpPost(URL_LOGIN, mCredentials);
		consumeEntity(mResponse);
		
		return (getCookieCount() > 1);
	}
	
	public boolean webmailLogin(String username, String password) throws Exception{
		List <NameValuePair> mCredentials = new ArrayList <NameValuePair>();
		mCredentials.add(new BasicNameValuePair("login_username", username));
		mCredentials.add(new BasicNameValuePair("secretkey", password));
		mResponse = httpPost(URL_WLOGIN, mCredentials);
		consumeEntity(mResponse);
		
		mResponse = httpPost(URL_REDIRECT, mCredentials);
		consumeEntity(mResponse);
		
		return (getCookieCount() > 1);
	}
	
	public void consumeEntity(HttpResponse response) throws Exception{
		//TODO Remove after Testing
		System.out.println("Login form get: " + response.getStatusLine());
		HttpEntity entity = response.getEntity();
		if (entity != null) 
		    entity.consumeContent();
	}
	
	//TODO BETTER THIS (Repetitive)
	public String getContent() throws Exception{
		mResponse = httpGet(URL_CONTENT);
		return EntityUtils.toString(mResponse.getEntity());
	}
	public String getFolders() throws Exception{
		mResponse = httpGet(URL_FOLDERS);
		return EntityUtils.toString(mResponse.getEntity());
	}
	
	public String getHTMLfromURL(String url) throws Exception{
		mResponse = httpGet(URL_BASE + url);
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
	
	public int getCookieCount(){
		List<Cookie> cookies = mCookieStore.getCookies();
		return cookies.size();
 	}
}
