package me.donggyeong.indexer.service;

import java.util.List;

import org.opensearch.client.opensearch.core.BulkResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.donggyeong.indexer.dto.ConsumedItemResponse;
import me.donggyeong.indexer.dto.IndexedItemRequest;
import me.donggyeong.indexer.enums.IndexingState;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulingService {
	private final ConsumedItemService consumedItemService;
	private final OpenSearchService openSearchService;
	private final IndexedItemService indexedItemService;

	@Scheduled(cron = "0/10 * * * * ?")
	@Transactional
	public void scheduleIndexing() {
		List<ConsumedItemResponse> consumedItemResponseList = consumedItemService.findByIndexingStateOrderByConsumedAt(IndexingState.PENDING);
		if (CollectionUtils.isEmpty(consumedItemResponseList)) {
			return;
		}

		BulkResponse bulkResponse = openSearchService.requestBulkIndexing(consumedItemResponseList);

		List<IndexedItemRequest> indexedItemRequestList = bulkResponse.items().stream().map(IndexedItemRequest::new).toList();
		for (IndexedItemRequest indexedItemRequest : indexedItemRequestList) {
			indexedItemService.save(indexedItemRequest);
		}

		for (ConsumedItemResponse consumedItemResponse : consumedItemResponseList){
			consumedItemService.updateIndexingStatus(consumedItemResponse, IndexingState.COMPLETED);
		}

		log.info("[ Complete bulk indexing ]");
	}
}
