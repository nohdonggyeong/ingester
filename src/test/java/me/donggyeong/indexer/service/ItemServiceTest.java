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

import me.donggyeong.indexer.dto.ItemRequest;
import me.donggyeong.indexer.dto.ItemResponse;
import me.donggyeong.indexer.enums.Action;
import me.donggyeong.indexer.repository.ItemRepository;

@SpringBootTest
class ItemServiceTest {
	@Autowired
	private ItemService itemService;
	@Autowired
	private ItemRepository itemRepository;

	@AfterEach
	void tearDown() {
		itemRepository.deleteAll();
	}

	@Test
	void save() {
		// given
		Map<String, Object> documentBody = new HashMap<>();
		documentBody.put("category", "test-category");
		documentBody.put("title", "test-title");
		documentBody.put("description", "test-description");
		ItemRequest itemRequest = new ItemRequest(Action.CREATE, "blog", "1", documentBody);

		// when
		ItemResponse itemResponse = itemService.save(itemRequest);

		// then
		assertNotNull(itemResponse);
		assertMapEquals(documentBody, itemResponse.getDocBody());
		assertNotNull(itemResponse.getConsumedAt());
	}

	@Test
	void findByIndexingStateOrderByConsumedAt() {
		// given
		ZonedDateTime offsetTime = ZonedDateTime.now(ZoneId.of("UTC"));
		Map<String, Object> documentBody = new HashMap<>();
		documentBody.put("category", "test-category");
		documentBody.put("title", "test-title");
		documentBody.put("description", "test-description");
		ItemRequest itemRequest = new ItemRequest(Action.CREATE, "blog", "1", documentBody);
		itemService.save(itemRequest);

		// when
		List<ItemResponse> itemResponseList = itemService.findByIndexingStateOrderByConsumedAt(
			IndexingState.PENDING);

		// then
		assertNotNull(itemResponseList);
		assertFalse(itemResponseList.isEmpty());
		assertEquals(1, itemResponseList.size());

		ItemResponse itemResponse = itemResponseList.getFirst();
		assertEquals(Action.CREATE, itemResponse.getAction());
		assertEquals("blog", itemResponse.getTarget());
		assertEquals("1", itemResponse.getDocId());
		assertMapEquals(documentBody, itemResponse.getDocBody());
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