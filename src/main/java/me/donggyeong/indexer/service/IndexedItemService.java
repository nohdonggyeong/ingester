package me.donggyeong.indexer.service;

import me.donggyeong.indexer.dto.IndexedItemRequest;
import me.donggyeong.indexer.dto.IndexedItemResponse;

public interface IndexedItemService {
	IndexedItemResponse save(IndexedItemRequest indexedItemRequest);
}
