package com.plantec.uwm;

public class Mail {
	private String From;
	private String Receieved;
	private String Subject;
	private String url;
	
	public String getFrom() {
		return From;
	}
	public void setFrom(String from) {
		From = from;
	}
	public String getReceieved() {
		return Receieved;
	}
	public void setReceieved(String receieved) {
		Receieved = receieved;
	}
	public String getSubject() {
		return Subject;
	}
	public void setSubject(String subject) {
		Subject = subject;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
