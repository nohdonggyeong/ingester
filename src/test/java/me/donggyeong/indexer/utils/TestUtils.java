package me.donggyeong.indexer.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

public class TestUtils {
	public static final String TARGET = "test";
	public static final String INDEX = "index_for_test";
	public static final String DOC_ID_1 = "test-id-1";
	public static final String DOC_ID_2 = "test-id-2";
	public static final String DOC_ID_3 = "test-id-3";
	public static final Map<String, Object> DOC_BODY = Map.of(
		"category", "test-category",
		"title", "test-title",
		"description", "test-description"
	);
	public static final String JSON_ROOT_PATH = "$";
	public static final String JSON_FIRST_ELEMENT_PATH = "$[0]";
}
