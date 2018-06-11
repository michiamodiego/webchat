package com.dsantoro.example.webchat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.dsantoro.example.webchat.websockethandler.TextBasedChatHandler;
import com.dsantoro.example.webchat.websockethandler.model.chainbuilder.ChatMessageHandlerChainBuilder;
import com.dsantoro.example.webchat.websockethandler.model.chatmessagehandler.ChatMessageHandler;
import com.dsantoro.example.webchat.websockethandler.model.chatmessagehandler.SimpleChatMessageHandler;
import com.dsantoro.example.webchat.websockethandler.model.chatmessageprocessor.impl.ChangeUsernameChatMessageProcessor;
import com.dsantoro.example.webchat.websockethandler.model.chatmessageprocessor.impl.SendAllChatMessageProcessor;
import com.dsantoro.example.webchat.websockethandler.model.chatmessageprocessor.impl.SendToUserMessageProcessor;
import com.dsantoro.example.webchat.websockethandler.model.chatmessageprocessor.impl.UnknownRequestChatMessageProcessor;
import com.dsantoro.example.webchat.websockethandler.model.chatmessageprocessor.impl.UserListChatMessageProcessor;
import com.dsantoro.example.webchat.websockethandler.model.chatmessageprocessor.impl.WhoIAmChatMessageProcessor;
import com.dsantoro.example.webchat.websockethandler.model.usernamegenerator.RandomUsernameGenerator;
import com.dsantoro.example.webchat.websockethandler.model.usernamegenerator.UsernameGenerator;

@Configuration
@EnableWebSocket
@ComponentScan(basePackages = {"com.dsantoro.example.webchat.websockethandler"})
public class WebSocketConfig implements WebSocketConfigurer {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private TextBasedChatHandler textBasedChatHandler;

	@Bean
	public UsernameGenerator randomUserGenerator() {

		return new RandomUsernameGenerator("Guest", "", System.currentTimeMillis(), 10000);

	}

	@Bean
	public ChatMessageHandler chatMessageHandler() {

		return new ChatMessageHandlerChainBuilder(messageSource)
				.addChatMessageHandler(
						new SimpleChatMessageHandler(
								new ChangeUsernameChatMessageProcessor(messageSource)
								)
						)
				.addChatMessageHandler(
						new SimpleChatMessageHandler(
								new SendToUserMessageProcessor(messageSource))
						)
				.addChatMessageHandler(
						new SimpleChatMessageHandler(
								new SendAllChatMessageProcessor(messageSource)
								)
						)
				.addChatMessageHandler(
						new SimpleChatMessageHandler(
								new UserListChatMessageProcessor(messageSource)
								)
						)
				.addChatMessageHandler(
						new SimpleChatMessageHandler(new WhoIAmChatMessageProcessor(messageSource)
								)
						)
				.addChatMessageHandler(
						new SimpleChatMessageHandler(
								new UnknownRequestChatMessageProcessor(messageSource)
								)
						)
				.build();

	}

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

		registry.addHandler(textBasedChatHandler, "/chat");

	}

}