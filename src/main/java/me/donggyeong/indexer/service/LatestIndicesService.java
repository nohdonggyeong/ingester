package me.donggyeong.indexer.service;

import me.donggyeong.indexer.dto.LatestIndicesResponse;

public interface LatestIndicesService {
	LatestIndicesResponse createLatestIndex(String source);
	LatestIndicesResponse getLatestIndexBySource(String source);
	LatestIndicesResponse updateLastIndexedAt(String source);
}