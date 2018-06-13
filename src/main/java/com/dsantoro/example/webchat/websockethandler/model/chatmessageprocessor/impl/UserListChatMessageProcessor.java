package com.dsantoro.example.webchat.websockethandler.model.chatmessageprocessor.impl;

import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;

import com.dsantoro.example.webchat.websockethandler.model.ChatUserSession;
import com.dsantoro.example.webchat.websockethandler.model.chatcontext.ChatContext;
import com.dsantoro.example.webchat.websockethandler.model.chatmessageprocessor.SimpleChatMessageProcessor;
import com.dsantoro.example.webchat.websockethandler.model.utils.ChatMessageUtils;

public class UserListChatMessageProcessor extends SimpleChatMessageProcessor {

	private static final Logger logger = LogManager.getLogger(UserListChatMessageProcessor.class);

	public static String PATTERN = "\\/userlist";

	public UserListChatMessageProcessor(MessageSource messageSource) {

		super(messageSource);

	}

	public boolean supports(String message) {

		logger.debug("supports", message);

		return Pattern.matches(PATTERN, message);

	}

	public void processMessage(ChatUserSession chatUserSession, String message, ChatContext chatContext) {

		logger.debug("processMessage");

		chatUserSession.send(
				ChatMessageUtils.createUserListMessage(
						chatContext
						.getChatUserSessionList()
						.stream()
						.map(u -> u.getUsername())
						.collect(Collectors.toList())
						)
				);

	}

}
