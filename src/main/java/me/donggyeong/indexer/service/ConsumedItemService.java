package me.donggyeong.indexer.service;

import java.util.List;

import me.donggyeong.indexer.dto.ConsumedItemRequest;
import me.donggyeong.indexer.dto.ConsumedItemResponse;
import me.donggyeong.indexer.enums.IndexingState;

public interface ConsumedItemService {
	ConsumedItemResponse save(ConsumedItemRequest consumedItemRequest);
	List<ConsumedItemResponse> findByIndexingStateOrderByConsumedAt(IndexingState indexingState);
	void updateIndexingStatus(ConsumedItemResponse consumedItemResponse, IndexingState indexingState);
}
