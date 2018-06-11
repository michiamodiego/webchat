package com.dsantoro.example.webchat.websockethandler.model.usernamegenerator;

import java.util.Random;

public class RandomUsernameGenerator implements UsernameGenerator {

	private String prefix;
	private String suffix;
	private Random random;
	private int bound;

	public RandomUsernameGenerator(String prefix, String suffix, long seed, int bound) {

		this.prefix = prefix;
		this.suffix = suffix;
		this.random = new Random(seed);
		this.bound = bound;

	}

	public String getPrefix() {
		return prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public int getBound() {
		return bound;
	}

	@Override
	public String getUsername() {

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(prefix);
		stringBuilder.append(random.nextInt(bound));
		stringBuilder.append(suffix);

		return stringBuilder.toString();

	}

}
