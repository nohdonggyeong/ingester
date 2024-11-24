package me.donggyeong.indexer.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import me.donggyeong.indexer.dto.SourceDataRequest;

@Service
@Slf4j
public class KafkaConsumerService {
	@KafkaListener(topics = "${spring.kafka.template.default-topic}", groupId = "${spring.kafka.consumer.group-id}")
	public void consume(@Payload SourceDataRequest message, @Headers MessageHeaders messageHeaders) {
		log.info(message.getAction().toString());
		log.info(message.getDocument().toString());
	}
}