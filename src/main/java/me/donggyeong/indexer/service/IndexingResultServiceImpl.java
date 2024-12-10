package me.donggyeong.indexer.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.donggyeong.indexer.dto.IndexingResultRequest;
import me.donggyeong.indexer.dto.IndexingResultResponse;
import me.donggyeong.indexer.entity.IndexingResult;
import me.donggyeong.indexer.repository.IndexingResultRepository;

@Service
@RequiredArgsConstructor
public class IndexingResultServiceImpl implements IndexingResultService{
	private final IndexingResultRepository indexingResultRepository;

	@Override
	@Transactional
	public IndexingResultResponse save(IndexingResultRequest indexingResultRequest) {
		IndexingResult indexingResult = indexingResultRequest.toEntity();
		indexingResult = indexingResultRepository.save(indexingResult);
		return new IndexingResultResponse(indexingResult);
	}
}
