package com.dsantoro.example.webchat.websockethandler.model.chatmessageprocessor;

import org.springframework.context.MessageSource;

public abstract class SimpleChatMessageProcessor implements ChatMessageProcessor {

	private MessageSource messageSource;

	public SimpleChatMessageProcessor(MessageSource messageSource) {

		this.messageSource = messageSource;

	}

	public MessageSource getMessageSource() {

		return messageSource;

	}

}
