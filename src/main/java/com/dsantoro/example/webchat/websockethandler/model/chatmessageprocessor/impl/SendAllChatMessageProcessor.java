package com.dsantoro.example.webchat.websockethandler.model.chatmessageprocessor.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;

import com.dsantoro.example.webchat.websockethandler.model.ChatMessageUtils;
import com.dsantoro.example.webchat.websockethandler.model.ChatUserSession;
import com.dsantoro.example.webchat.websockethandler.model.chatcontext.ChatContext;
import com.dsantoro.example.webchat.websockethandler.model.chatmessageprocessor.SimpleChatMessageProcessor;

public class SendAllChatMessageProcessor extends SimpleChatMessageProcessor {

	private static final Logger logger = LogManager.getLogger(SendAllChatMessageProcessor.class);

	public static String PATTERN = "\\/message (?<message>(.)+)";

	public SendAllChatMessageProcessor(MessageSource messageSource) {

		super(messageSource);

	}

	public boolean supports(String message) {

		logger.debug("supports", message);

		return Pattern.matches(PATTERN, message);

	}

	public void processMessage(ChatUserSession chatUserSession, String message, ChatContext chatContext) {

		logger.debug("processMessage");

		Matcher matcher = Pattern.compile(PATTERN).matcher(message);
		matcher.find();

		String messageToSend = matcher.group("message");

		String command = ChatMessageUtils.createChatMessage(
				chatUserSession.getUsername(), 
				messageToSend);

		chatContext.getChatUserSessionList().stream().forEach(u -> {

			u.send(command);

		});

	}

}
