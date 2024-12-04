package me.donggyeong.indexer.service;

import me.donggyeong.indexer.dto.IndexDetailsResponse;

public interface IndexDetailsService {
	IndexDetailsResponse createIndexDetails(String source);
	IndexDetailsResponse getIndexDetailsBySource(String source);
	IndexDetailsResponse updateLastIndexedAt(String source);
}