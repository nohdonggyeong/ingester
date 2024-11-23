package me.donggyeong.indexer.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;

@Getter
public enum Action {
	INDEX("index"),
	CREATE("create"),
	UPDATE("update"),
	DELETE("delete");

	private final String action;

	Action(String action) {
		this.action = action;
	}

	@JsonCreator
	public static Action from(String value) {
		for (Action action : Action.values()) {
			if (action.getAction().equalsIgnoreCase(value)) {
				return action;
			}
		}
		throw new IllegalArgumentException("Unknown value: " + value);
	}
}
