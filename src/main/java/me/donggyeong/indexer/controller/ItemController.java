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
import me.donggyeong.indexer.dto.ItemRequest;
import me.donggyeong.indexer.dto.ItemResponse;
import me.donggyeong.indexer.service.ItemService;

@RestController
@RequestMapping("/api/v1/item")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
	private final ItemService itemService;

	@PostMapping
	@Deprecated(since = "v1", forRemoval = true)
	public ResponseEntity<ItemResponse> save(@Valid @RequestBody ItemRequest itemRequest) {
		log.info("[ Requested ] {}", itemRequest.toString());
		ItemResponse itemResponse = itemService.save(itemRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(itemResponse);
	}
}
