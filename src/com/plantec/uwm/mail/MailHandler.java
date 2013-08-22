package com.plantec.uwm.mail;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.plantec.uwm.http.HttpManager;

public class MailHandler {
	private HttpManager http;
	private ArrayList<Mail> mailList;
	private Mail mail;

	public MailHandler(){
		Document doc = null;
		try {
			doc = Jsoup.parse(http.getContent());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
						mail.setSender(data.text());
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
	}
	
	
	
	public ArrayList<Mail> getMail(){
		return mailList;
	}
}
