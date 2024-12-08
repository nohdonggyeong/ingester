package me.donggyeong.indexer.enums;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;

@Getter
public enum Status {
	PENDING("pending"),
	PROCESSED("processed"),
	FAILED("failed");

	private final String name;

	Status(String name) {
		this.name = name;
	}

	@JsonCreator
	public static Status of(String status) {
		return Arrays.stream(Status.values())
			.filter(i -> i.name.equalsIgnoreCase(status))
			.findAny()
			.orElse(null);
	}
}
