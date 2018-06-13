package com.dsantoro.example.webchat.websockethandler.model.chatmessageprocessor.impl;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;

import com.dsantoro.example.webchat.websockethandler.model.ChatUserSession;
import com.dsantoro.example.webchat.websockethandler.model.chatcontext.ChatContext;
import com.dsantoro.example.webchat.websockethandler.model.chatmessageprocessor.SimpleChatMessageProcessor;
import com.dsantoro.example.webchat.websockethandler.model.utils.ChatMessageUtils;

public class SendToUserMessageProcessor extends SimpleChatMessageProcessor {

	private static final Logger logger = LogManager.getLogger(SendToUserMessageProcessor.class);

	public static String PATTERN = "\\/pvt (?<message>(.)+) \\/sender (?<sender>[A-Za-z0-9]+) \\/recipient (?<recipient>[A-Za-z0-9]+)";

	public SendToUserMessageProcessor(MessageSource messageSource) {

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
		String recipient = matcher.group("recipient");

		ChatUserSession recipientSession = chatContext
				.getChatUserSessionByUsername(recipient);

		if(!Objects.isNull(recipientSession)) {

			String chatMessage = ChatMessageUtils.createPrivateMessage(
					messageToSend, 
					chatUserSession.getUsername(), 
					recipientSession.getUsername()
					);

			chatUserSession.send(chatMessage);
			recipientSession.send(chatMessage);

		} else {

			chatUserSession.send(
					ChatMessageUtils.createErrorMessage(
							getMessageSource()
							.getMessage("sendto.error", null, Locale.ITALY)
							)
					);

		}

	}

}
