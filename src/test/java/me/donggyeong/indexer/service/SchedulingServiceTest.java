package me.donggyeong.indexer.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.opensearch.client.opensearch.indices.CreateIndexResponse;
import org.opensearch.client.opensearch.indices.DeleteIndexResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import me.donggyeong.indexer.dto.ItemRequest;
import me.donggyeong.indexer.dto.ItemResponse;
import me.donggyeong.indexer.enums.Action;
import me.donggyeong.indexer.repository.ItemRepository;
import me.donggyeong.indexer.utils.TestUtils;

@SpringBootTest
class SchedulingServiceTest {

	@Autowired
	private SchedulingService schedulingService;

	@Autowired
	private OpenSearchService openSearchService;

	@Autowired
	private ItemService itemService;

	@Autowired
	private ItemRepository itemRepository;

	@AfterEach
	void tearDown() {
		itemRepository.deleteAll();
	}

	@Test
	void testScheduleIndexing() {
		// Given
		CreateIndexResponse createIndexResponse = openSearchService.createIndexWithAlias(TestUtils.TARGET);

		List<ItemRequest> itemRequestList = new ArrayList<>();
		for (long i = 1; i <= 3; i++) {
			ItemRequest itemRequest = new ItemRequest();
			itemRequest.setAction(Action.CREATE);
			itemRequest.setTarget(TestUtils.TARGET);
			itemRequest.setDocId(String.valueOf(i));
			itemRequest.setDocBody(TestUtils.DOC_BODY);
			itemRequestList.add(itemRequest);
		}

		// Save the items to the database for testing
		for (ItemRequest item : itemRequestList) {
			itemService.save(item); // Assuming save method exists to persist the item
		}

		// When
		schedulingService.scheduleIndexing();

		openSearchService.deleteIndex(createIndexResponse.index());

		// Then
		List<ItemResponse> indexedItemList = itemService.findAll();

		assertEquals(itemRequestList.size(), indexedItemList.size());
		for (ItemResponse indexedItem : indexedItemList) {
			assertNotNull(indexedItem.getIndexedAt());
			assertEquals(HttpStatus.CREATED.value(), indexedItem.getStatus()); // Assuming action is updated to INDEX after processing
		}
	}
}
