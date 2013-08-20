package com.plantec.uwm.server;

import java.io.IOException;
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

public class BaseHTTPCommand {
	private CookieStore mCookieStore = new BasicCookieStore();
	private HttpContext mLocalContext = new BasicHttpContext();
	private DefaultHttpClient mHttpClient = new DefaultHttpClient();
	private HttpResponse response = null;
	private HttpEntity entity;
	private HttpGet mHttpGet;
	private HttpPost mHttpPost;
	
	public BaseHTTPCommand(){
		mLocalContext.setAttribute(ClientContext.COOKIE_STORE, mCookieStore);
		mHttpClient.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		HttpGet httpget = new HttpGet("https://www.uvic.ca/cas/login?service=http%3A%2F%2Fwww.uvic.ca%2F");
		try {
			response = mHttpClient.execute(httpget, mLocalContext);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		entity = response.getEntity();
		if (entity != null) {
		    try {
				entity.consumeContent();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	//TODO Handle incorrect logins
	public void login(String username, String password) throws IOException{
		HttpPost httpost = new HttpPost("https://www.uvic.ca/cas/login?service=http%3A%2F%2Fdev.uvic.ca%2Fcurrent-students%2Findex.php");
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("username", username));
		nvps.add(new BasicNameValuePair("password", password));
		nvps.add(new BasicNameValuePair("lt", "e1s1"));
		nvps.add(new BasicNameValuePair("_eventId", "submit"));
		httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		response = mHttpClient.execute(httpost, mLocalContext);
		entity = response.getEntity();
		if (entity != null) {
		    entity.consumeContent();
		}
	}
	
	public HttpEntity webmailLogin() throws IOException{
		HttpPost httpost2 = new HttpPost("https://wm3.uvic.ca/src/CASlogin.php");
		List <NameValuePair> nvpss = new ArrayList <NameValuePair>();
		nvpss.add(new BasicNameValuePair("login_username", "cplante"));
		nvpss.add(new BasicNameValuePair("secretkey", "Dragoon1"));
		httpost2.setEntity(new UrlEncodedFormEntity(nvpss, HTTP.UTF_8));
		response = mHttpClient.execute(httpost2, mLocalContext);
		entity = response.getEntity();
		System.out.println("Login form get: " + response.getStatusLine());
		if (entity != null) {
		    entity.consumeContent();
		}
		httpost2 = new HttpPost("https://wm3.uvic.ca/src/redirect.php");
		httpost2.setEntity(new UrlEncodedFormEntity(nvpss, HTTP.UTF_8));
		response = mHttpClient.execute(httpost2, mLocalContext);
		entity = response.getEntity();
		System.out.println("Login form get: " + response.getStatusLine());
		if (entity != null) {
		    entity.consumeContent();
		}
		mHttpGet = new HttpGet("https://wm3.uvic.ca/src/right_main.php");
		response = mHttpClient.execute(mHttpGet, mLocalContext);
		return response.getEntity();	
	}
}
