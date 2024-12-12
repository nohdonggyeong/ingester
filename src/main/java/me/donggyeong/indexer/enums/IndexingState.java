package me.donggyeong.indexer.enums;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;

@Getter
public enum IndexingState {
	PENDING("pending"),
	COMPLETED("completed");

	private final String name;

	IndexingState(String name) {
		this.name = name;
	}

	@JsonCreator
	public static IndexingState of(String indexingState) {
		return Arrays.stream(IndexingState.values())
			.filter(i -> i.name.equalsIgnoreCase(indexingState))
			.findAny()
			.orElse(null);
	}
}
