package com.dsantoro.example.webchat.websockethandler;

import java.util.Observable;
import java.util.Observer;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.dsantoro.example.webchat.websockethandler.model.ChatMessageUtils;
import com.dsantoro.example.webchat.websockethandler.model.ChatUserSession;
import com.dsantoro.example.webchat.websockethandler.model.chatcontext.ChatContext;
import com.dsantoro.example.webchat.websockethandler.model.chatcontext.ChatContextEvent;
import com.dsantoro.example.webchat.websockethandler.model.chatcontext.ChatContextEventType;
import com.dsantoro.example.webchat.websockethandler.model.chatmessagehandler.ChatMessageHandler;

@Component
public class TextBasedChatHandler extends TextWebSocketHandler implements Observer {

	private static final Logger logger = LogManager.getLogger(TextBasedChatHandler.class);

	@Autowired
	private ChatContext chatContext;

	@Autowired
	private ChatMessageHandler chatMessageHandler;

	@PostConstruct
	public void postConstruct() {

		chatContext.addObserver(this);

	}

	@Override
	public void afterConnectionClosed(WebSocketSession webSocketSession,
			CloseStatus status) throws Exception {

		logger.debug("afterConnectionClosed", webSocketSession.getId());

		chatContext.removeChatUserSession(webSocketSession);

	}

	@Override
	public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {

		logger.debug("afterConnectionEstablished", webSocketSession.getId());

		chatContext.addChatUserSession(webSocketSession);

	}

	@Override
	protected void handleTextMessage(WebSocketSession webSocketSession, TextMessage message) throws Exception {

		logger.debug("handleTextMessage", webSocketSession.getId());

		chatContext
		.getChatUserSession(webSocketSession)
		.handleMessage(
				message, 
				chatContext, 
				chatMessageHandler
				);

	}

	@Override
	public boolean supportsPartialMessages() {

		return false;

	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception)
			throws Exception {

		logger.debug("#ERROR ", exception);

	}

	@Override
	public void update(Observable o, Object e) {

		ChatContext chatContext = (ChatContext) o;
		ChatContextEvent chatContextEvent = (ChatContextEvent) e;
		ChatContextEventType chatContextEventType = chatContextEvent.getEventType();
		ChatUserSession chatUserSession = chatContextEvent.getChatUserSession();
		String username = chatUserSession.getUsername();
		String chatCommand = 
				ChatContextEventType.JOIN.equals(chatContextEventType) ? 
						ChatMessageUtils.createJoinMessage(username): 
							ChatMessageUtils.createQuitMessage(username);

						chatContext
						.getChatUserSessionList()
						.stream()
						.filter(c -> !username.equals(c.getUsername()))
						.forEach(u -> {

							u.send(chatCommand);

						});	

	}

}