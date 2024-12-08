package me.donggyeong.indexer.service;

import java.time.ZonedDateTime;
import java.util.List;

import org.opensearch.client.opensearch.core.BulkResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.donggyeong.indexer.dto.IndexingItemResponse;

@Service
@RequiredArgsConstructor
public class SchedulingService {
	private final LatestIndicesService latestIndicesService;
	private final IndexingItemService indexingItemService;
	private final OpenSearchService openSearchService;

	@Scheduled(cron = "0/10 * * * * ?")
	public void scheduleIndexing() {
		ZonedDateTime latestIndexedTime = latestIndicesService.getLatestOrDefaultIndexedTime();
		List<IndexingItemResponse> indexingItemResponseList = indexingItemService.findIndexingItemAfter(latestIndexedTime);
		BulkResponse bulkResponse = openSearchService.requestBulk(indexingItemResponseList);
		// TODO: CREATE indexing_result
	}
}
