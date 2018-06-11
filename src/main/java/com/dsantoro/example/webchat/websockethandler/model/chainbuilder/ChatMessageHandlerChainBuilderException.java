package com.dsantoro.example.webchat.websockethandler.model.chainbuilder;

public class ChatMessageHandlerChainBuilderException extends RuntimeException {

	private static final long serialVersionUID = 2747825376213866251L;

	public ChatMessageHandlerChainBuilderException() {

		super();

	}

	public ChatMessageHandlerChainBuilderException(String s) {

		super(s);

	}

	public ChatMessageHandlerChainBuilderException(String s, Throwable throwable) {

		super(s, throwable);

	}

	public ChatMessageHandlerChainBuilderException(Throwable throwable) {

		super(throwable);

	}

}
