package com.plantec.uwm.mail;

public class Mail {
	private String sender;
	private String subject;
	private String received;
	private String url;
	
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getReceieved() {
		return received;
	}
	public void setReceieved(String received) {
		this.received = received;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
