package me.donggyeong.indexer.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.donggyeong.indexer.dto.IndexingItemRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {
	private final IndexingItemService indexingItemService;

	@KafkaListener(topics = "${spring.kafka.template.default-topic}", groupId = "${spring.kafka.consumer.group-id}")
	public void consume(@Payload @Valid IndexingItemRequest message, @Headers MessageHeaders messageHeaders) {
		log.info("Received Kafka message: {}", message.toString());
		indexingItemService.saveIndexingItem(message);
	}
}