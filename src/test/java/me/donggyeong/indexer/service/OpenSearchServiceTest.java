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
import org.opensearch.client.opensearch.core.BulkResponse;
import org.opensearch.client.opensearch.indices.CreateIndexResponse;
import org.opensearch.client.opensearch.indices.DeleteIndexResponse;
import org.opensearch.client.transport.endpoints.BooleanResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;
import me.donggyeong.indexer.dto.SourceDataResponse;
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
		List<SourceDataResponse> sourceDataResponseList = new ArrayList<>();

		// 인덱싱할 문서 추가
		Map<String, Object> indexData = new HashMap<>();
		indexData.put("category", "test-category");
		indexData.put("title", "test-title");
		indexData.put("description", "test-description");
		sourceDataResponseList.add(new SourceDataResponse(Action.INDEX, "hub", 1L, indexData, null));

		// 생성할 문서 추가
		Map<String, Object> createData = new HashMap<>();
		createData.put("category", "test-category");
		createData.put("title", "test-title");
		createData.put("description", "test-description");
		sourceDataResponseList.add(new SourceDataResponse(Action.CREATE, "hub", 2L, createData, null));

		// 업데이트할 문서 추가
		Map<String, Object> updateData = new HashMap<>();
		updateData.put("category", "test-updated-category");
		updateData.put("title", "test-updated-title");
		updateData.put("description", "test-updated-description");
		sourceDataResponseList.add(new SourceDataResponse(Action.UPDATE, "hub", 1L, updateData, null));

		// 삭제할 문서 추가
		sourceDataResponseList.add(new SourceDataResponse(Action.DELETE, "hub", 2L, null, null));

		// When
		BulkResponse bulkResponse = openSearchService.requestBulk(sourceDataResponseList);

		// Then
		assertAll(
			() -> assertNotNull(bulkResponse),
			() -> assertEquals(sourceDataResponseList.size(), bulkResponse.items().size()),
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