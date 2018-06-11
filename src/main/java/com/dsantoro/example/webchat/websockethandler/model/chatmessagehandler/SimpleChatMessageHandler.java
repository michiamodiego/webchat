package com.dsantoro.example.webchat.websockethandler.model.chatmessagehandler;

import com.dsantoro.example.webchat.websockethandler.model.ChatUserSession;
import com.dsantoro.example.webchat.websockethandler.model.chatcontext.ChatContext;
import com.dsantoro.example.webchat.websockethandler.model.chatmessageprocessor.ChatMessageProcessor;

public class SimpleChatMessageHandler extends ChatMessageHandler {

	private ChatMessageProcessor chatMessageProcessor;

	public SimpleChatMessageHandler(ChatMessageProcessor chatMessageProcessor) {

		this.chatMessageProcessor = chatMessageProcessor;

	}

	@Override
	public void handleMessage(ChatUserSession chatUserSession, String message, ChatContext chatContext) {

		if(chatMessageProcessor.supports(message)) {

			chatMessageProcessor.processMessage(chatUserSession, message, chatContext);

		} else {

			getSuccessor().handleMessage(chatUserSession, message, chatContext);

		}

	}

}
