package me.donggyeong.indexer.enums;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;

@Getter
public enum Action {
	INDEX("index"),
	CREATE("create"),
	UPDATE("update"),
	DELETE("delete");

	private final String name;

	Action(String name) {
		this.name = name;
	}

	@JsonCreator
	public static Action of(String action) {
		return Arrays.stream(Action.values())
			.filter(i -> i.name.equalsIgnoreCase(action))
			.findAny()
			.orElse(null);
	}
}
