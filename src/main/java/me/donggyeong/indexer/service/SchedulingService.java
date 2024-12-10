package me.donggyeong.indexer.service;

import java.time.ZonedDateTime;
import java.util.List;

import org.opensearch.client.opensearch.core.BulkResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.donggyeong.indexer.dto.IndexingItemRequest;
import me.donggyeong.indexer.dto.IndexingItemResponse;
import me.donggyeong.indexer.dto.IndexingResultRequest;
import me.donggyeong.indexer.dto.IndexingResultResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulingService {
	private final IndexingItemService indexingItemService;
	private final OpenSearchService openSearchService;
	private final IndexingResultService indexingResultService;

	@Scheduled(cron = "0/10 * * * * ?")
	public void scheduleIndexing() {
		ZonedDateTime lastIndexedAt = null;
		List<IndexingItemResponse> indexingItemResponseList = indexingItemService.getIndexingItemListAfter(lastIndexedAt);

		List<IndexingItemRequest> indexingItemRequestList = indexingItemResponseList.stream().map(IndexingItemRequest::new).toList();
		BulkResponse bulkResponse = openSearchService.requestBulk(indexingItemRequestList);

		IndexingResultRequest indexingResultRequest = new IndexingResultRequest(bulkResponse);
		IndexingResultResponse indexingResultResponse = indexingResultService.saveIndexingResult(indexingResultRequest);
		log.info("[ Completed ]: {}", indexingResultResponse.toString());
	}
}
