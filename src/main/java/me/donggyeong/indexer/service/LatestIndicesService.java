package me.donggyeong.indexer.service;

import java.util.List;

import me.donggyeong.indexer.dto.LatestIndicesResponse;

public interface LatestIndicesService {
	LatestIndicesResponse createLatestIndex(String source);
	LatestIndicesResponse getLatestIndexBySource(String source);
	List<LatestIndicesResponse> getLatestIndices();
	LatestIndicesResponse updateLatestIndex(String source);
	LatestIndicesResponse updateLastIndexedAt(String source);
}