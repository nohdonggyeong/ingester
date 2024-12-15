package me.donggyeong.indexer.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import me.donggyeong.indexer.dto.ItemRequest;
import me.donggyeong.indexer.dto.ItemResponse;
import me.donggyeong.indexer.enums.Action;
import me.donggyeong.indexer.repository.ItemRepository;
import me.donggyeong.indexer.utils.TestUtils;

@SpringBootTest
class KafkaConsumerServiceTest {

	@Autowired
	private KafkaConsumerService kafkaConsumerService;

	@Autowired
	private ItemService itemService;

	@Autowired
	private ItemRepository itemRepository;

	@AfterEach
	void tearDown() {
		itemRepository.deleteAll();
	}

	@Test
	void testConsume() {
		// Given
		ItemRequest itemRequest = new ItemRequest();
		itemRequest.setAction(Action.CREATE);
		itemRequest.setTarget(TestUtils.TARGET);
		itemRequest.setDocId(TestUtils.DOC_ID_1);
		itemRequest.setDocBody(TestUtils.DOC_BODY);

		// When
		kafkaConsumerService.consume(itemRequest, null);

		// Then
		ItemResponse savedItemRequest = itemService.findAll().getFirst();

		assertNotNull(savedItemRequest);
		assertEquals(itemRequest.getDocId(), savedItemRequest.getDocId());
		assertEquals(itemRequest.getTarget(), savedItemRequest.getTarget());
		assertEquals(itemRequest.getAction(), savedItemRequest.getAction());
	}
}
