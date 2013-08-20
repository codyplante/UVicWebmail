package com.plantec.uwm;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.plantec.uwm.server.BaseHTTPCommand;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity {
	BaseHTTPCommand http = new BaseHTTPCommand();
	ListView mMailList = (ListView) findViewById(R.id.main_list);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Intent intent = getIntent();
		
		try {
			http.login(intent.getExtras().getString("username"), intent.getExtras().getString("password"));
		} catch (IOException e) {
			//TODO osmething here
		}
		
		Document doc = null;
		try {
			doc = Jsoup.parse(EntityUtils.toString(http.webmailLogin()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<Mail> mailList = new ArrayList<Mail>();
		Mail mail;
		Elements list2 = doc.getElementsByAttributeValue("valign", "top");
		int i = 0;
		for (Element link2 : list2){
			System.out.println(link2.toString());
			System.out.println(link2.text());
			Elements info = link2.getElementsByTag("TD");
			if (info.isEmpty()){
				continue;
			}
			i = 0;
			mail = new Mail();
			for (Element data : info){
				switch(i){
					case 1:
						mail.setFrom(data.text());
						break;
					case 2:
						mail.setReceieved(data.text());
						break;
					case 4:
						mail.setSubject(data.text());
						Elements yo = data.getElementsByTag("a");
						mail.setUrl(yo.first().attr("href").toString());
						break;
				}
				i++;
			}
			mailList.add(mail);
		}
		for (i = 0; i < mailList.size() ; i++){
			mail = mailList.get(i);
			System.out.println(mail.getFrom() + "\n" + mail.getReceieved() + "\n" + mail.getSubject() + "\n" + mail.getUrl() + "\n\n");
		}
	}
}
