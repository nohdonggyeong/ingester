package me.donggyeong.indexer.service;

import java.util.List;

import me.donggyeong.indexer.dto.RawDataRequest;
import me.donggyeong.indexer.dto.RawDataResponse;

public interface RawDataService {
	RawDataResponse createRawData(RawDataRequest rawDataRequest);
	List<RawDataResponse> getRawDataAfterOffset();
}
