package me.donggyeong.indexer.service;

import java.time.ZonedDateTime;
import java.util.List;

import me.donggyeong.indexer.dto.SourceDataRequest;
import me.donggyeong.indexer.dto.SourceDataResponse;

public interface SourceDataService {
	SourceDataResponse createSourceData(SourceDataRequest sourceDataRequest);
	List<SourceDataResponse> getSourceDataAfterOffset(ZonedDateTime latestIndexedTime);
}
