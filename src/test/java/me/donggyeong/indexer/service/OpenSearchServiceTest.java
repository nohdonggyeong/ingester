package me.donggyeong.indexer.service;

import static org.junit.jupiter.api.Assertions.*;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;
import me.donggyeong.indexer.dto.IndexingItemRequest;
import me.donggyeong.indexer.enums.Action;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
class OpenSearchServiceTest {
	@Autowired
	OpenSearchService openSearchService;
	private static final String TARGET_NAME = "test";
	private static final String INDEX_NAME = "index_for_test";

	@Order(1)
	@Test
	void createIndex() {
		// Given & When
		CreateIndexResponse createIndexResponse = openSearchService.createIndex(TARGET_NAME);
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
	void requestBulk() {
		// Given
		List<IndexingItemRequest> indexingItemRequestList = new ArrayList<>();

		// 인덱싱할 문서 추가
		Map<String, Object> indexDocumentBody = new HashMap<>();
		indexDocumentBody.put("category", "test-index-category");
		indexDocumentBody.put("title", "test-index-title");
		indexDocumentBody.put("description", "test-index-description");
		indexingItemRequestList.add(
			new IndexingItemRequest(
				Action.INDEX,
				TARGET_NAME,
				1L,
				indexDocumentBody
			)
		);

		// 생성할 문서 추가
		Map<String, Object> createDocumentBody = new HashMap<>();
		createDocumentBody.put("category", "test-create-category");
		createDocumentBody.put("title", "test-create-title");
		createDocumentBody.put("description", "test-create-description");
		indexingItemRequestList.add(
			new IndexingItemRequest(
				Action.CREATE,
				TARGET_NAME,
				2L,
				createDocumentBody
			)
		);

		// 업데이트할 문서 추가
		Map<String, Object> updateDocumentBody = new HashMap<>();
		updateDocumentBody.put("category", "test-update-category");
		updateDocumentBody.put("title", "test-update-title");
		updateDocumentBody.put("description", "test-update-description");
		indexingItemRequestList.add(
			new IndexingItemRequest(
				Action.UPDATE,
				TARGET_NAME,
				1L,
				updateDocumentBody
			)
		);

		// 삭제할 문서 추가
		indexingItemRequestList.add(
			new IndexingItemRequest(
				Action.DELETE,
				TARGET_NAME,
				2L,
				null
			)
		);

		// When
		BulkResponse bulkResponse = openSearchService.requestBulk(indexingItemRequestList);

		// Then
		assertAll(
			() -> assertNotNull(bulkResponse),
			() -> assertEquals(indexingItemRequestList.size(), bulkResponse.items().size()),
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
	void findAllAliases() {
		// Given & When
		AliasesResponse aliasesResponse = openSearchService.findAllAliases();
		log.debug("aliasesResponse : [{}]", aliasesResponse);

		// Then
		assertAll(
			() -> assertNotNull(aliasesResponse)
		);
	}
}