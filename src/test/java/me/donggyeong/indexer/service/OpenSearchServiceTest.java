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
import me.donggyeong.indexer.dto.ItemResponse;
import me.donggyeong.indexer.enums.Action;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
class OpenSearchServiceTest {
	@Autowired
	OpenSearchService openSearchService;
	private static final String TARGET = "test";
	private static final String INDEX = "index_for_test";

	@Order(1)
	@Test
	void createIndexWithAlias() {
		// Given & When
		CreateIndexResponse createIndexResponse = openSearchService.createIndexWithAlias(TARGET);
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
	void requestBulkIndexing() {
		// Given
		List<ItemResponse> itemResponseList = new ArrayList<>();

		// 인덱싱할 문서 추가
		Map<String, Object> indexDocumentBody = new HashMap<>();
		indexDocumentBody.put("category", "test-index-category");
		indexDocumentBody.put("title", "test-index-title");
		indexDocumentBody.put("description", "test-index-description");
		itemResponseList.add(
			new ItemResponse(
				1L,
				Action.INDEX,
				TARGET,
				"1",
				indexDocumentBody,
				null,
				null,
				null,
				null,
				null,
				null
			)
		);

		// 생성할 문서 추가
		Map<String, Object> createDocumentBody = new HashMap<>();
		createDocumentBody.put("category", "test-create-category");
		createDocumentBody.put("title", "test-create-title");
		createDocumentBody.put("description", "test-create-description");
		itemResponseList.add(
			new ItemResponse(
				2L,
				Action.CREATE,
				TARGET,
				"2",
				createDocumentBody,
				null,
				null,
				null,
				null,
				null,
				null
			)
		);

		// 업데이트할 문서 추가
		Map<String, Object> updateDocumentBody = new HashMap<>();
		updateDocumentBody.put("category", "test-update-category");
		updateDocumentBody.put("title", "test-update-title");
		updateDocumentBody.put("description", "test-update-description");
		itemResponseList.add(
			new ItemResponse(
				3L,
				Action.UPDATE,
				TARGET,
				"1",
				updateDocumentBody,
				null,
				null,
				null,
				null,
				null,
				null
			)
		);

		// 삭제할 문서 추가
		itemResponseList.add(
			new ItemResponse(
				4L,
				Action.DELETE,
				TARGET,
				"2",
				null,
				null,
				null,
				null,
				null,
				null,
				null
			)
		);

		// When
		BulkResponse bulkResponse = openSearchService.requestBulkIndexing(itemResponseList);

		// Then
		assertAll(
			() -> assertNotNull(bulkResponse),
			() -> assertEquals(itemResponseList.size(), bulkResponse.items().size()),
			() -> assertTrue(bulkResponse.items().stream().allMatch(item -> item.error() == null)),
			() -> log.debug("Bulk operation completed successfully with response: {}", bulkResponse)
		);
	}

	@Order(4)
	@Test
	void deleteIndex() {
		// Given & When
		DeleteIndexResponse deleteIndexResponse = openSearchService.deleteIndex(INDEX);
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