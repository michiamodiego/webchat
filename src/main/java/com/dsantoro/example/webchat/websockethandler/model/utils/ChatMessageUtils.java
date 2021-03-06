package com.dsantoro.example.webchat.websockethandler.model.utils;

import java.util.List;

public class ChatMessageUtils {

	public static String createInfoMessage(String message) {

		return new ChatMessageBuilder()
				.addMessagePart("info", message)
				.getChatMessage();

	}

	public static String createUserListMessage(List<String> usernameList) {

		return new ChatMessageBuilder()
				.addMessagePart("userlist", 
						usernameList
						.stream()
						.reduce((u1, u2) -> u1 + ", " + u2)
						.get()
						).getChatMessage();

	}

	public static String createChatMessage(String username, String message) {

		return new ChatMessageBuilder()
				.addMessagePart("message", message)
				.addMessagePart("sender", username)
				.getChatMessage();

	}

	public static String createErrorMessage(String message) {

		return new ChatMessageBuilder()
				.addMessagePart("error", message)
				.getChatMessage();

	}

	public static String createPrivateMessage(String messageToSend, String sender, String recipient) {

		return new ChatMessageBuilder()
				.addMessagePart("pvt", messageToSend)
				.addMessagePart("sender", sender)
				.addMessagePart("recipient", recipient)
				.getChatMessage();

	}

	public static String createWhoIAmMessage(String username) {

		return new ChatMessageBuilder()
				.addMessagePart("whoiam", username)
				.getChatMessage();

	}

	public static String createUsernameMessage(String username) {

		return new ChatMessageBuilder()
				.addMessagePart("username", username)
				.getChatMessage();

	}

	public static String createJoinMessage(String username) {

		return new ChatMessageBuilder()
				.addMessagePart("join", username)
				.getChatMessage();

	}

	public static String createQuitMessage(String username) {

		return new ChatMessageBuilder()
				.addMessagePart("quit", username)
				.getChatMessage();

	}

}
