package com.dsantoro.example.webchat.websockethandler.model.chatmessageprocessor.impl;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;

import com.dsantoro.example.webchat.websockethandler.model.ChatUserSession;
import com.dsantoro.example.webchat.websockethandler.model.chatcontext.ChatContext;
import com.dsantoro.example.webchat.websockethandler.model.chatmessageprocessor.SimpleChatMessageProcessor;
import com.dsantoro.example.webchat.websockethandler.model.utils.ChatMessageUtils;

public class ChangeUsernameChatMessageProcessor extends SimpleChatMessageProcessor {

	private static final Logger logger = LogManager.getLogger(ChangeUsernameChatMessageProcessor.class);

	public static String PATTERN = "\\/username (?<username>[A-Za-z0-9]+)";

	public ChangeUsernameChatMessageProcessor(MessageSource messageSource) {

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

		String newUsername = matcher.group("username");

		if(Objects.isNull(chatContext.getChatUserSessionByUsername(newUsername))) {

			String oldUsername = chatUserSession.getUsername();
			chatUserSession.setUsername(newUsername);

			String chatMessage = ChatMessageUtils
					.createUserListMessage(chatContext
							.getChatUserSessionList()
							.stream()
							.filter(u -> !u.getUsername().equals(oldUsername))
							.map(u -> u.getUsername())
							.collect(Collectors.toList()));

			chatContext
			.getChatUserSessionList()
			.stream()
			.forEach(u -> {

				u.send(chatMessage);

			});

			chatUserSession.send(
					ChatMessageUtils.createUsernameMessage(newUsername)
					);

		} else {

			chatUserSession.send(
					ChatMessageUtils.createErrorMessage(
							getMessageSource()
							.getMessage("changeusername.error", null, Locale.ITALY)
							)
					);

		}

	}

}
