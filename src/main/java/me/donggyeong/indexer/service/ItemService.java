package me.donggyeong.indexer.service;

import java.time.ZonedDateTime;
import java.util.List;

import org.opensearch.client.opensearch.core.bulk.BulkResponseItem;

import me.donggyeong.indexer.dto.ItemRequest;
import me.donggyeong.indexer.dto.ItemResponse;

public interface ItemService {
	ItemResponse save(ItemRequest itemRequest);
	List<ItemResponse> findAll();
	List<ItemResponse> findByStatusIsNullOrderByConsumedAtAsc();
	ItemResponse update(Long id, BulkResponseItem bulkResponseItem, ZonedDateTime utcNow);
}
