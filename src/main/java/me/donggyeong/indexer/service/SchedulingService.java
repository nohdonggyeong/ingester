package me.donggyeong.indexer.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.opensearch.client.opensearch.core.BulkResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.donggyeong.indexer.dto.ItemResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulingService {
	private final ItemService itemService;
	private final OpenSearchService openSearchService;

	@Scheduled(cron = "0/10 * * * * ?")
	@Transactional
	public void scheduleIndexing() {
		List<ItemResponse> itemResponseList = itemService.findByStatusIsNullOrderByConsumedAtAsc();

		if (CollectionUtils.isEmpty(itemResponseList)) {
			return;
		}
		BulkResponse bulkResponse = openSearchService.requestBulkIndexing(itemResponseList);

		ZonedDateTime utcNow = ZonedDateTime.now(ZoneId.of("UTC"));
		for (int i = 0; i < itemResponseList.size(); ++i) {
			itemService.update(itemResponseList.get(i).getId(), bulkResponse.items().get(i), utcNow);
		}
		log.info("[ Indexing complete! ]");
	}
}
