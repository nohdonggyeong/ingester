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
import me.donggyeong.indexer.dto.IndexingItemRequest;
import me.donggyeong.indexer.dto.IndexingItemResponse;
import me.donggyeong.indexer.service.IndexingItemService;

@RestController
@RequestMapping("/api/v1/indexing-item")
@RequiredArgsConstructor
@Slf4j
public class IndexingItemController {
	private final IndexingItemService indexingItemService;

	@PostMapping
	@Deprecated(since = "v1", forRemoval = true)
	public ResponseEntity<IndexingItemResponse> saveIndexingItem(@Valid @RequestBody IndexingItemRequest indexingItemRequest) {
		log.info("Requested indexing item: {}", indexingItemRequest.toString());
		IndexingItemResponse indexingItemResponse = indexingItemService.saveIndexingItem(indexingItemRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(indexingItemResponse);
	}
}
