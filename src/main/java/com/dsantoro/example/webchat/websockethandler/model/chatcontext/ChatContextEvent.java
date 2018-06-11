package com.dsantoro.example.webchat.websockethandler.model.chatcontext;

import com.dsantoro.example.webchat.websockethandler.model.ChatUserSession;

public class ChatContextEvent {

	private ChatContextEventType chatContextEventType;
	private ChatUserSession chatUserSession;

	public ChatContextEvent(ChatContextEventType chatContextEventType, ChatUserSession chatUserSession) {

		this.chatContextEventType = chatContextEventType;
		this.chatUserSession = chatUserSession;

	}

	public ChatContextEventType getEventType() {

		return chatContextEventType;

	}

	public ChatUserSession getChatUserSession() {

		return chatUserSession;

	}

}
