package com.dsantoro.example.webchat.websockethandler.model.chatmessageprocessor;

import com.dsantoro.example.webchat.websockethandler.model.ChatUserSession;
import com.dsantoro.example.webchat.websockethandler.model.chatcontext.ChatContext;

public interface ChatMessageProcessor {

	abstract boolean supports(String message);

	abstract void processMessage(
			ChatUserSession chatUserSession, 
			String message, 
			ChatContext chatContext);

}
