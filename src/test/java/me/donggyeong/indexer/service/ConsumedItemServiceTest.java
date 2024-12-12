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

import me.donggyeong.indexer.dto.ConsumedItemRequest;
import me.donggyeong.indexer.dto.ConsumedItemResponse;
import me.donggyeong.indexer.enums.Action;
import me.donggyeong.indexer.enums.IndexingState;
import me.donggyeong.indexer.repository.ConsumedItemRepository;

@SpringBootTest
class ConsumedItemServiceTest {
	@Autowired
	private ConsumedItemService consumedItemService;
	@Autowired
	private ConsumedItemRepository consumedItemRepository;

	@AfterEach
	void tearDown() {
		consumedItemRepository.deleteAll();
	}

	@Test
	void save() {
		// given
		Map<String, Object> documentBody = new HashMap<>();
		documentBody.put("category", "test-category");
		documentBody.put("title", "test-title");
		documentBody.put("description", "test-description");
		ConsumedItemRequest consumedItemRequest = new ConsumedItemRequest(Action.CREATE, "blog", "1", documentBody);

		// when
		ConsumedItemResponse consumedItemResponse = consumedItemService.save(consumedItemRequest);

		// then
		assertNotNull(consumedItemResponse);
		assertMapEquals(documentBody, consumedItemResponse.getDocBody());
		assertNotNull(consumedItemResponse.getConsumedAt());
	}

	@Test
	void findByIndexingStateOrderByConsumedAt() {
		// given
		ZonedDateTime offsetTime = ZonedDateTime.now(ZoneId.of("UTC"));
		Map<String, Object> documentBody = new HashMap<>();
		documentBody.put("category", "test-category");
		documentBody.put("title", "test-title");
		documentBody.put("description", "test-description");
		ConsumedItemRequest consumedItemRequest = new ConsumedItemRequest(Action.CREATE, "blog", "1", documentBody);
		consumedItemService.save(consumedItemRequest);

		// when
		List<ConsumedItemResponse> consumedItemResponseList = consumedItemService.findByIndexingStateOrderByConsumedAt(
			IndexingState.PENDING);

		// then
		assertNotNull(consumedItemResponseList);
		assertFalse(consumedItemResponseList.isEmpty());
		assertEquals(1, consumedItemResponseList.size());

		ConsumedItemResponse consumedItemResponse = consumedItemResponseList.getFirst();
		assertEquals(Action.CREATE, consumedItemResponse.getAction());
		assertEquals("blog", consumedItemResponse.getTarget());
		assertEquals("1", consumedItemResponse.getDocId());
		assertMapEquals(documentBody, consumedItemResponse.getDocBody());
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