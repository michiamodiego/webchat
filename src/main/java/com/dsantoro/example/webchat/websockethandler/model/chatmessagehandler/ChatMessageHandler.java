package com.dsantoro.example.webchat.websockethandler.model.chatmessagehandler;

import com.dsantoro.example.webchat.websockethandler.model.ChatUserSession;
import com.dsantoro.example.webchat.websockethandler.model.chatcontext.ChatContext;

public abstract class ChatMessageHandler {

	private ChatMessageHandler successor = null;

	public void setSuccessor(ChatMessageHandler successor) {

		this.successor = successor;

	}

	public ChatMessageHandler getSuccessor() {

		return successor;

	}

	public abstract void handleMessage(
			ChatUserSession chatUserSession, 
			String message, 
			ChatContext chatContext
			);

}
