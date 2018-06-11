package com.dsantoro.example.webchat.websockethandler.model;

import java.util.List;

public class ChatMessageUtils {

	public static String createInfoMessage(String message) {

		return "/info " + message;

	}

	public static String createUserListMessage(List<String> usernameList) {

		return "/userlist " + usernameList.stream().reduce((u1, u2) -> u1 + ", " + u2).get();

	}

	public static String createChatMessage(String username, String message) {

		return "/message " + message + " /sender " + username;

	}

	public static String createErrorMessage(String message) {

		return "/error " + message;

	}

	public static String createPrivateMessage(String messageToSend, String sender, String recipient) {

		return "/pvt " + messageToSend + " /sender " + sender + " /recipient " + recipient;

	}

	public static String createWhoIAmMessage(String username) {

		return "/whoiam " + username;

	}

	public static String createUsernameMessage(String username) {

		return "/username " + username;

	}

	public static String createJoinMessage(String username) {

		return "/join " + username;

	}

	public static String createQuitMessage(String username) {

		return "/quit " + username;

	}

}
