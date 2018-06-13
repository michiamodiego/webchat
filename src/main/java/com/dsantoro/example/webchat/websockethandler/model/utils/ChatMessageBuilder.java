package com.dsantoro.example.webchat.websockethandler.model.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ChatMessageBuilder {

	private HashMap<String, String> messagePartList = new LinkedHashMap<String, String>();

	public ChatMessageBuilder addMessagePart(String part, String value) {

		messagePartList.put(part, value);

		return this;

	}

	public ChatMessageBuilder addMessagePart(String part) {

		messagePartList.put(part, null);

		return this;

	}

	public String toString() {

		String chatMessage = 
				Arrays.asList(
						messagePartList
						.keySet()
						.toArray()
						)
				.stream()
				.map(k -> {

					StringBuilder stringBuilder = new StringBuilder();

					stringBuilder.append("/");
					stringBuilder.append(k);

					if(messagePartList.get(k) != null) {

						stringBuilder.append(" ");
						stringBuilder.append(messagePartList.get(k));

					}

					return stringBuilder.toString();

				})
				.reduce((p1, p2) -> {

					StringBuilder stringBuilder = new StringBuilder();
					stringBuilder.append(p1);
					stringBuilder.append(" ");
					stringBuilder.append(p2);

					return stringBuilder.toString();

				})
				.get();

		return chatMessage;

	}

}
