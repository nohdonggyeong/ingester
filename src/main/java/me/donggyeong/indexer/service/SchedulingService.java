package me.donggyeong.indexer.service;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.donggyeong.indexer.dto.SourceDataResponse;

@Service
@RequiredArgsConstructor
public class SchedulingService {
	private final SourceDataService sourceDataService;

	@Scheduled(cron = "0/10 * * * * ?")
	public void scheduleIndexing() {
		List<SourceDataResponse> sourceDataResponseList = sourceDataService.getSourceDataAfterOffset();
		for (SourceDataResponse sourceDataResponse : sourceDataResponseList) {
			// TODO: Request indexing to OpenSearch client
		}
	}
}
