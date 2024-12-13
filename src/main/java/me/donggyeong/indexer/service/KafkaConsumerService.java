package me.donggyeong.indexer.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.donggyeong.indexer.dto.ItemRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {
	private final ItemService itemService;

	@KafkaListener(topics = "${spring.kafka.template.default-topic}", groupId = "${spring.kafka.consumer.group-id}")
	public void consume(@Payload @Valid ItemRequest message, @Headers MessageHeaders messageHeaders) {
		log.info("[ Received item ]: {}", message.toString());
		itemService.save(message);
	}
}