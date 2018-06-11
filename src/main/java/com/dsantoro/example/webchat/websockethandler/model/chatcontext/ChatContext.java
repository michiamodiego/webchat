package com.dsantoro.example.webchat.websockethandler.model.chatcontext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import com.dsantoro.example.webchat.websockethandler.model.ChatUserSession;
import com.dsantoro.example.webchat.websockethandler.model.usernamegenerator.UsernameGenerator;

@Component
public class ChatContext extends Observable {

	// Observable here is implemented as a push model, no pull model: setChanged method will never be used and hasChanged method will always return false
	// The choice to make ChatContext a push model system is derived from the fact that ChatContext is used in a multi-thread environemnt and there is no need to manage the observerable'state through synchronized methods
	// Unfortunately the Observable interface provided in java.util is dirty and it implements a push model and pull model at the same time

	private static final Logger logger = LogManager.getLogger(ChatContext.class);

	private Map<String, ChatUserSession> chatUserSessionMap;

	private List<Observer> observerList;

	private boolean changed;

	@Autowired 
	@Qualifier("randomUserGenerator")
	private UsernameGenerator usernameGenerator; 

	@PostConstruct
	public void postConstruct() {

		chatUserSessionMap = Collections.synchronizedMap(new HashMap<String, ChatUserSession>());
		observerList = new ArrayList<Observer>();

		clearChanged();

	}

	@PreDestroy
	public void preDestroy() {

		Arrays.asList(
				chatUserSessionMap
				.keySet()
				.toArray()
				)
		.stream()
		.forEach(k -> {

			chatUserSessionMap
			.get(k)
			.close();

		});

	}

	public void addChatUserSession(WebSocketSession webSocketSession) {

		logger.debug("addChatUserSession");

		ChatUserSession chatUserSession = new ChatUserSession(webSocketSession, usernameGenerator);

		chatUserSessionMap.put(webSocketSession.getId(), chatUserSession);

		ChatContextEvent chatContextEvent = new ChatContextEvent(
				ChatContextEventType.JOIN, 
				chatUserSession
				);

		// setChanged();

		this.notifyObservers(chatContextEvent);

	}

	public void removeChatUserSession(WebSocketSession webSocketSession) {

		logger.debug("removeChatUserSession");

		ChatUserSession chatUserSession = chatUserSessionMap.get(webSocketSession.getId());

		chatUserSessionMap.remove(webSocketSession.getId());

		ChatContextEvent chatContextEvent = new ChatContextEvent(
				ChatContextEventType.QUIT, 
				chatUserSession
				);

		// setChanged();

		this.notifyObservers(chatContextEvent);

	}

	public ChatUserSession getChatUserSession(WebSocketSession webSocketSession) {

		logger.debug("getChatUserSession");

		return chatUserSessionMap.get(webSocketSession.getId());

	}

	public ChatUserSession getChatUserSessionByUsername(String username) {

		return chatUserSessionMap
				.values()
				.stream()
				.filter(u -> username
						.toLowerCase()
						.equals(
								u
								.getUsername()
								.toLowerCase()
								)
						)
				.findFirst()
				.orElse(null);

	}

	public List<ChatUserSession> getChatUserSessionList() {

		return new ArrayList<ChatUserSession>(chatUserSessionMap.values());

	}

	public void addObserver(Observer o) {

		observerList.add(o);

	}

	public void deleteObserver(Observer observer) {

		observerList.remove(observer);

	}

	public void deleteObservers() {

		observerList.clear();

	}

	public void notifyObservers() {

		notifyObservers(null);

	}

	public void notifyObservers(Object event) {

		ChatContext chatContext = this;
		ChatContextEvent chatContextEvent = (ChatContextEvent) event;

		observerList.stream().forEach(o -> {

			o.update(chatContext, chatContextEvent);

		});

		clearChanged();

	}

	public int countObservers() {

		return observerList.size();

	}

	protected void setChanged() {

		changed = true;

	}

	public boolean hasChanged() {

		return changed;

	}

	protected void clearChanged() {

		changed = false;

	}

}
