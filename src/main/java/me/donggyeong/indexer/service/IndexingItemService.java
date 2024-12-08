package me.donggyeong.indexer.service;

import java.time.ZonedDateTime;
import java.util.List;

import me.donggyeong.indexer.dto.IndexingItemRequest;
import me.donggyeong.indexer.dto.IndexingItemResponse;

public interface IndexingItemService {
	IndexingItemResponse saveIndexingItem(IndexingItemRequest indexingItemRequest);
	List<IndexingItemResponse> findIndexingItemAfter(ZonedDateTime lastIndexedAt);
}
