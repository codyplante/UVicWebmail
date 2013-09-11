package com.plantec.uwm.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.plantec.uwm.http.HttpManager;

public class HTMLParser {
	private HttpManager http;
	
	public HTMLParser(){
		http = HttpManager.getInstance();
	}
	
	public String parseMailContent(String url) {
		Document doc = null;
		try {
			doc = Jsoup.parse(http.getHTMLfromURL(url));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Elements data = doc.getElementsByAttributeValue("cellspacing", "5");
		
		return data.html();
	}
}
