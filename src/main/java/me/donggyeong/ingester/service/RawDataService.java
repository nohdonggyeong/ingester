package me.donggyeong.ingester.service;

import java.util.List;

import me.donggyeong.ingester.dto.RawDataRequest;
import me.donggyeong.ingester.dto.RawDataResponse;

public interface RawDataService {
	RawDataResponse createRawData(RawDataRequest rawDataRequest);
	List<RawDataResponse> getRawDataCandidateList();
}
