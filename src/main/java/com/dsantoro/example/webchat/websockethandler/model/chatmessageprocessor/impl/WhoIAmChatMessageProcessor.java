package com.dsantoro.example.webchat.websockethandler.model.chatmessageprocessor.impl;

import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;

import com.dsantoro.example.webchat.websockethandler.model.ChatMessageUtils;
import com.dsantoro.example.webchat.websockethandler.model.ChatUserSession;
import com.dsantoro.example.webchat.websockethandler.model.chatcontext.ChatContext;
import com.dsantoro.example.webchat.websockethandler.model.chatmessageprocessor.SimpleChatMessageProcessor;

public class WhoIAmChatMessageProcessor extends SimpleChatMessageProcessor {

	private static final Logger logger = LogManager.getLogger(WhoIAmChatMessageProcessor.class);

	public static String PATTERN = "\\/whoiam";

	public WhoIAmChatMessageProcessor(MessageSource messageSource) {

		super(messageSource);

	}

	public boolean supports(String message) {

		logger.debug("supports", message);

		return Pattern.matches(PATTERN, message);

	}

	public void processMessage(ChatUserSession chatUserSession, String message, ChatContext chatContext) {

		String chatMessage = ChatMessageUtils.createWhoIAmMessage(
				chatUserSession.getUsername()
				);

		logger.debug("processMessage", chatMessage);

		chatUserSession.send(
				chatMessage
				);

	}

}
