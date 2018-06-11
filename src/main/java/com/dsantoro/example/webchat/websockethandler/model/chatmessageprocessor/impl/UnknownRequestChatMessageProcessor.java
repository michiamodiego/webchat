package com.dsantoro.example.webchat.websockethandler.model.chatmessageprocessor.impl;

import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;

import com.dsantoro.example.webchat.websockethandler.model.ChatMessageUtils;
import com.dsantoro.example.webchat.websockethandler.model.ChatUserSession;
import com.dsantoro.example.webchat.websockethandler.model.chatcontext.ChatContext;
import com.dsantoro.example.webchat.websockethandler.model.chatmessageprocessor.SimpleChatMessageProcessor;

public class UnknownRequestChatMessageProcessor extends SimpleChatMessageProcessor {

	private static final Logger logger = LogManager.getLogger(UnknownRequestChatMessageProcessor.class);

	public UnknownRequestChatMessageProcessor(MessageSource messageSource) {

		super(messageSource);

	}

	public boolean supports(String message) {

		logger.debug("supports", message);

		return true;

	}

	public void processMessage(ChatUserSession chatUserSession, String message, ChatContext chatContext) {

		logger.debug("processMessage");

		chatUserSession.send(
				ChatMessageUtils.createErrorMessage(
						getMessageSource()
						.getMessage("unknownrequest.error", null, Locale.ITALY)
						));

	}

}
