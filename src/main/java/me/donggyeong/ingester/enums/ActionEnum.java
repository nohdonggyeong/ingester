package me.donggyeong.ingester.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ActionEnum {
	INDEX("index"),
	CREATE("create"),
	UPDATE("update"),
	DELETE("delete");

	private final String action;

	ActionEnum(String action) {
		this.action = action;
	}

	public String getAction() {
		return action;
	}

	@JsonCreator
	public static ActionEnum from(String value) {
		for (ActionEnum actionEnum : ActionEnum.values()) {
			if (actionEnum.getAction().equalsIgnoreCase(value)) {
				return actionEnum;
			}
		}
		throw new IllegalArgumentException("Unknown value: " + value);
	}
}
