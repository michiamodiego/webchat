package com.dsantoro.example.webchat.websockethandler.model.chainbuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

import org.springframework.context.MessageSource;

import com.dsantoro.example.webchat.websockethandler.model.chatmessagehandler.ChatMessageHandler;

public class ChatMessageHandlerChainBuilder {

	private MessageSource messageSource;
	private List<ChatMessageHandler> chatMessageHandlerList;

	public ChatMessageHandlerChainBuilder(MessageSource messageSource) {

		this.messageSource = messageSource;

		chatMessageHandlerList = new ArrayList<ChatMessageHandler>();

	}

	public ChatMessageHandlerChainBuilder addChatMessageHandler(ChatMessageHandler chatMessageHandler) {

		chatMessageHandlerList.add(chatMessageHandler);

		return this;

	}

	public ChatMessageHandler build() {

		if(chatMessageHandlerList.size() != 0) {

			IntStream
			.range(-(chatMessageHandlerList.size() - 1), 0)
			.map(i -> -i)
			.forEach(i -> {

				chatMessageHandlerList
				.get(i-1)
				.setSuccessor(
						chatMessageHandlerList.get(i)
						);

			});

			return chatMessageHandlerList.get(0);

		}

		throw new ChatMessageHandlerChainBuilderException(messageSource.getMessage("chainbuilder.error", null, Locale.ITALY));

	}

}
