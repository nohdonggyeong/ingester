package me.donggyeong.indexer.service;

import me.donggyeong.indexer.dto.IndexingResultRequest;
import me.donggyeong.indexer.dto.IndexingResultResponse;

public interface IndexingResultService {
	IndexingResultResponse save(IndexingResultRequest indexingResultRequest);
}
