package me.donggyeong.indexer.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.opensearch.client.opensearch.cat.AliasesResponse;
import org.opensearch.client.opensearch.core.BulkResponse;
import org.opensearch.client.opensearch.indices.CreateIndexResponse;
import org.opensearch.client.opensearch.indices.DeleteIndexResponse;
import org.opensearch.client.transport.endpoints.BooleanResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;
import me.donggyeong.indexer.dto.IndexingItemResponse;
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
		List<IndexingItemResponse> indexingItemResponseList = new ArrayList<>();

		// 인덱싱할 문서 추가
		Map<String, Object> indexDocumentBody = new HashMap<>();
		indexDocumentBody.put("category", "test-index-category");
		indexDocumentBody.put("title", "test-index-title");
		indexDocumentBody.put("description", "test-index-description");
		indexingItemResponseList.add(
			new IndexingItemResponse(
				Action.INDEX,
				"blog",
				1L,
				indexDocumentBody,
				ZonedDateTime.now(ZoneId.of("UTC"))
			)
		);

		// 생성할 문서 추가
		Map<String, Object> createDocumentBody = new HashMap<>();
		createDocumentBody.put("category", "test-create-category");
		createDocumentBody.put("title", "test-create-title");
		createDocumentBody.put("description", "test-create-description");
		indexingItemResponseList.add(
			new IndexingItemResponse(
				Action.CREATE,
				"blog",
				2L,
				createDocumentBody,
				ZonedDateTime.now(ZoneId.of("UTC"))
			)
		);

		// 업데이트할 문서 추가
		Map<String, Object> updateDocumentBody = new HashMap<>();
		updateDocumentBody.put("category", "test-update-category");
		updateDocumentBody.put("title", "test-update-title");
		updateDocumentBody.put("description", "test-update-description");
		indexingItemResponseList.add(
			new IndexingItemResponse(
				Action.UPDATE,
				"blog",
				1L,
				updateDocumentBody,
				ZonedDateTime.now(ZoneId.of("UTC"))
			)
		);

		// 삭제할 문서 추가
		indexingItemResponseList.add(
			new IndexingItemResponse(
				Action.DELETE,
				"blog",
				2L,
				null,
				ZonedDateTime.now(ZoneId.of("UTC"))
			)
		);

		// When
		BulkResponse bulkResponse = openSearchService.requestBulk(indexingItemResponseList);

		// Then
		assertAll(
			() -> assertNotNull(bulkResponse),
			() -> assertEquals(indexingItemResponseList.size(), bulkResponse.items().size()),
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

	@Test
	void getAliasList() {
		// Given & When
		AliasesResponse aliasesResponse = openSearchService.getAliasList();
		log.debug("aliasesResponse : [{}]", aliasesResponse);

		// Then
		assertAll(
			() -> assertNotNull(aliasesResponse)
		);
	}
}