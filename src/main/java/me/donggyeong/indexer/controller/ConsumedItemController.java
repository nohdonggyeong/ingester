package me.donggyeong.indexer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.donggyeong.indexer.dto.ConsumedItemRequest;
import me.donggyeong.indexer.dto.ConsumedItemResponse;
import me.donggyeong.indexer.service.ConsumedItemService;

@RestController
@RequestMapping("/api/v1/consumed-item")
@RequiredArgsConstructor
@Slf4j
public class ConsumedItemController {
	private final ConsumedItemService consumedItemService;

	@PostMapping
	@Deprecated(since = "v1", forRemoval = true)
	public ResponseEntity<ConsumedItemResponse> save(@Valid @RequestBody ConsumedItemRequest consumedItemRequest) {
		log.info("Requested consumed item: {}", consumedItemRequest.toString());
		ConsumedItemResponse consumedItemResponse = consumedItemService.save(consumedItemRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(consumedItemResponse);
	}
}
