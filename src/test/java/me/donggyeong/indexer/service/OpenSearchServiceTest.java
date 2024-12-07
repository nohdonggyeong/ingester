package me.donggyeong.indexer.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.opensearch.client.opensearch.core.BulkResponse;
import org.opensearch.client.opensearch.indices.CreateIndexResponse;
import org.opensearch.client.opensearch.indices.DeleteIndexResponse;
import org.opensearch.client.transport.endpoints.BooleanResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;
import me.donggyeong.indexer.enums.Action;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
class OpenSearchServiceTest {
	@Autowired
	OpenSearchService openSearchService;

	private static final String INDEX_NAME = "index-for-test";

	@Order(1)
	@Test
	void createIndex() {
		// Given & When
		CreateIndexResponse createIndexResponse = openSearchService.createIndex(INDEX_NAME);
		log.debug("createIndexResponse.index : [{}], createIndexResponse.acknowledged : [{}]"
			, createIndexResponse.index(), createIndexResponse.acknowledged());

		// Then
		assertAll(
			() -> assertNotNull(createIndexResponse),
			() -> assertNotNull(createIndexResponse.index()),
			() -> assertEquals(Boolean.TRUE, createIndexResponse.acknowledged())
		);
	}

	@Order(2)
	@Test
	void checkIndexExists() {
		// Given & When
		BooleanResponse booleanResponse = openSearchService.checkIndexExists(INDEX_NAME);
		log.debug("booleanResponse : [{}]", booleanResponse);

		// Then
		assertAll(
			() -> assertNotNull(booleanResponse),
			() -> assertTrue(booleanResponse.value())
		);
	}

	@Order(3)
	@Test
	void requestBulk() {
		// Given
		List<Map<String, Object>> dataList = new ArrayList<>();

		// 인덱싱할 문서 추가
		Map<String, Object> indexData = Map.of(
			"action", Action.INDEX.name(),
			"source", "hub",
			"id", 1L,
			"title", "index document"
		);
		dataList.add(indexData);

		// 생성할 문서 추가
		Map<String, Object> createData = Map.of(
			"action", Action.CREATE.name(),
			"source", "hub",
			"id", 2L,
			"title", "create document"
		);
		dataList.add(createData);

		// 업데이트할 문서 추가
		Map<String, Object> updateData = Map.of(
			"action", Action.UPDATE.name(),
			"source", "hub",
			"id", 1L,
			"title", "update document"
		);
		dataList.add(updateData);

		// 삭제할 문서 추가
		Map<String, Object> deleteData = Map.of(
			"action", Action.DELETE.name(),
			"source", "hub",
			"id", 2L
		);
		dataList.add(deleteData);

		// When
		BulkResponse bulkResponse = openSearchService.requestBulk(dataList);

		// Then
		assertAll(
			() -> assertNotNull(bulkResponse),
			() -> assertEquals(dataList.size(), bulkResponse.items().size()),
			() -> assertTrue(bulkResponse.items().stream().allMatch(item -> item.error() == null)),
			() -> log.debug("Bulk operation completed successfully with response: {}", bulkResponse)
		);
	}

	@Order(4)
	@Test
	void deleteIndex() {
		// Given & When
		DeleteIndexResponse deleteIndexResponse = openSearchService.deleteIndex(INDEX_NAME);
		log.debug("deleteIndexResponse.acknowledged : [{}]", deleteIndexResponse.acknowledged());

		// Then
		assertAll(
			() -> assertNotNull(deleteIndexResponse),
			() -> assertTrue(deleteIndexResponse.acknowledged())
		);
	}
}