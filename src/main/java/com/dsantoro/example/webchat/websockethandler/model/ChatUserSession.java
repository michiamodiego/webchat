package com.dsantoro.example.webchat.websockethandler.model;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.dsantoro.example.webchat.websockethandler.model.chatcontext.ChatContext;
import com.dsantoro.example.webchat.websockethandler.model.chatmessagehandler.ChatMessageHandler;
import com.dsantoro.example.webchat.websockethandler.model.usernamegenerator.UsernameGenerator;

public class ChatUserSession {

	private static final Logger logger = LogManager.getLogger(ChatUserSession.class);

	private WebSocketSession webSocketSession;
	private String username;

	public ChatUserSession(WebSocketSession webSocketSession, UsernameGenerator usernameGenerator) {

		this.webSocketSession = webSocketSession;
		this.username = usernameGenerator.getUsername();

	}

	public void setUsername(String username) {

		this.username = username;

	}

	public String getUsername() {

		return this.username;

	}

	public void handleMessage(TextMessage textMessage, ChatContext chatContext, ChatMessageHandler chatMessageHandler) {

		logger.debug("handleMessage di ChatUserSession", textMessage.getPayload());

		chatMessageHandler.handleMessage(this, textMessage.getPayload(), chatContext);

	}

	public void send(String message) {

		try {

			if(!isClosed()) {

				webSocketSession.sendMessage(new TextMessage(message));

			}

		} catch(IOException e) {

			logger.error("#EXPECTED_ERROR ", e);

		}

	}

	public void close() {

		try {

			webSocketSession.close();

		} catch (IOException e) {

			logger.error("#EXPECTED_ERROR ", e);

		}

	}

	public boolean isClosed() {

		return !webSocketSession.isOpen();

	}

}
