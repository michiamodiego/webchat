package com.dsantoro.example.webchat.websockethandler.model.chatcontext;

public enum ChatContextEventType {

	JOIN("JOIN"), 
	QUIT("QUIT");

	private String value;

	ChatContextEventType(String value) {

		this.value = value;

	}

	public String getValue() {

		return value;

	}

}
