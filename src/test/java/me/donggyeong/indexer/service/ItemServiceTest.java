package me.donggyeong.indexer.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.opensearch.client.opensearch.core.bulk.OperationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import me.donggyeong.indexer.dto.ItemRequest;
import me.donggyeong.indexer.dto.ItemResponse;
import me.donggyeong.indexer.entity.Item;
import me.donggyeong.indexer.enums.Action;
import me.donggyeong.indexer.repository.ItemRepository;
import me.donggyeong.indexer.utils.TestUtils;

import org.opensearch.client.opensearch.core.bulk.BulkResponseItem;
import org.springframework.http.HttpStatus;

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
		// Given
		ItemRequest itemRequest = new ItemRequest();
		itemRequest.setAction(Action.CREATE);
		itemRequest.setTarget(TestUtils.TARGET);
		itemRequest.setDocId(TestUtils.DOC_ID_1);
		itemRequest.setDocBody(TestUtils.DOC_BODY);

		// When
		ItemResponse itemResponse = itemService.save(itemRequest);

		// Then
		assertNotNull(itemResponse);
		assertMapEquals(itemResponse.getDocBody());
		assertNotNull(itemResponse.getConsumedAt());
	}

	@Test
	void findByStatusIsNullOrderByConsumedAtAsc() {
		// Given
		Item item1 = createItem(TestUtils.DOC_ID_1, new HashMap<>());
		Item item2 = createItem(TestUtils.DOC_ID_2, new HashMap<>());
		Item item3 = createItem(TestUtils.DOC_ID_3, new HashMap<>());

		item3.update(TestUtils.INDEX, HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase(), null, ZonedDateTime.now(
			ZoneId.of("UTC")));

		itemRepository.save(item1);
		itemRepository.save(item2);
		itemRepository.save(item3);

		// When
		List<ItemResponse> result = itemService.findByStatusIsNullOrderByConsumedAtAsc();

		// Then
		assertEquals(2, result.size());
		assertTrue(result.stream().allMatch(itemResponse -> itemResponse.getStatus() == null));
	}

	@Test
	void update() {
		// Given
		ItemRequest itemRequest = new ItemRequest();
		itemRequest.setAction(Action.CREATE);
		itemRequest.setTarget(TestUtils.TARGET);
		itemRequest.setDocId(TestUtils.DOC_ID_1);
		itemRequest.setDocBody(TestUtils.DOC_BODY);

		ItemResponse savedItemResponse = itemService.save(itemRequest);

		BulkResponseItem bulkResponseItem = createBulkResponseItem();

		// When
		ItemResponse updatedItemResponse = itemService.update(savedItemResponse.getId(), bulkResponseItem, ZonedDateTime.now());

		// Then
		assertNotNull(updatedItemResponse);
		assertEquals(savedItemResponse.getId(), updatedItemResponse.getId());
	}

	private Item createItem(String docId, Map<String, Object> docBody) {
		return Item.builder()
			.action(Action.CREATE)
			.target(TestUtils.TARGET)
			.docId(docId)
			.docBody(docBody)
			.build();
	}

	private BulkResponseItem createBulkResponseItem() {
		return BulkResponseItem.of(builder -> builder
			.operationType(OperationType.Update)
			.index(TestUtils.INDEX)
			.id(TestUtils.DOC_ID_1)
			.status(HttpStatus.OK.value())
			.result(HttpStatus.OK.getReasonPhrase())
		);
	}

	private void assertMapEquals(Map<String, Object> actual) {
		assertEquals(TestUtils.DOC_BODY.size(), actual.size());
		for (Map.Entry<String, Object> entry : TestUtils.DOC_BODY.entrySet()) {
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
