package me.donggyeong.indexer.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import me.donggyeong.indexer.dto.IndexingItemRequest;
import me.donggyeong.indexer.dto.IndexingItemResponse;
import me.donggyeong.indexer.enums.Action;
import me.donggyeong.indexer.repository.IndexingItemRepository;

@SpringBootTest
class IndexingItemServiceTest {
	@Autowired
	private IndexingItemService indexingItemService;
	@Autowired
	private IndexingItemRepository indexingItemRepository;

	@AfterEach
	void tearDown() {
		indexingItemRepository.deleteAll();
	}

	@Test
	void saveIndexingItem() {
		// given
		Map<String, Object> documentBody = new HashMap<>();
		documentBody.put("category", "test-category");
		documentBody.put("title", "test-title");
		documentBody.put("description", "test-description");
		IndexingItemRequest indexingItemRequest = new IndexingItemRequest(Action.CREATE, "blog", 1L, documentBody);

		// when
		IndexingItemResponse indexingItemResponse = indexingItemService.saveIndexingItem(indexingItemRequest);

		// then
		assertNotNull(indexingItemResponse);
		assertMapEquals(documentBody, indexingItemResponse.getDocumentBody());
		assertNotNull(indexingItemResponse.getConsumedAt());
	}

	@Test
	void getIndexingItemListAfter() {
		// given
		ZonedDateTime offsetTime = ZonedDateTime.now(ZoneId.of("UTC"));
		Map<String, Object> documentBody = new HashMap<>();
		documentBody.put("category", "test-category");
		documentBody.put("title", "test-title");
		documentBody.put("description", "test-description");
		IndexingItemRequest indexingItemRequest = new IndexingItemRequest(Action.CREATE, "blog", 1L, documentBody);
		indexingItemService.saveIndexingItem(indexingItemRequest);

		// when
		List<IndexingItemResponse> indexingItemResponseList = indexingItemService.getIndexingItemListAfter(offsetTime);

		// then
		assertNotNull(indexingItemResponseList);
		assertFalse(indexingItemResponseList.isEmpty());
		assertEquals(1, indexingItemResponseList.size());

		IndexingItemResponse indexingItemResponse = indexingItemResponseList.getFirst();
		assertEquals(Action.CREATE, indexingItemResponse.getAction());
		assertEquals("blog", indexingItemResponse.getTargetName());
		assertEquals(1L, indexingItemResponse.getDocumentId());
		assertMapEquals(documentBody, indexingItemResponse.getDocumentBody());
	}

	// Helper method to compare maps ignoring their specific implementations
	private void assertMapEquals(Map<String, Object> expected, Map<String, Object> actual) {
		assertEquals(expected.size(), actual.size());
		for (Map.Entry<String, Object> entry : expected.entrySet()) {
			assertTrue(actual.containsKey(entry.getKey()));
			Object expectedValue = entry.getValue();
			Object actualValue = actual.get(entry.getKey());
			if (expectedValue instanceof Number && actualValue instanceof Number) {
				assertEquals(((Number) expectedValue).longValue(), ((Number) actualValue).longValue());
			} else {
				assertEquals(expectedValue, actualValue);
			}
		}
	}
}