package me.donggyeong.indexer.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.donggyeong.indexer.entity.IndexingItem;
import me.donggyeong.indexer.dto.IndexingItemRequest;
import me.donggyeong.indexer.dto.IndexingItemResponse;
import me.donggyeong.indexer.repository.IndexingItemRepository;

@Service
@RequiredArgsConstructor
public class IndexingItemServiceImpl implements IndexingItemService {
	private final IndexingItemRepository indexingItemRepository;

	@Override
	@Transactional
	public IndexingItemResponse saveIndexingItem(IndexingItemRequest indexingItemRequest) {
		IndexingItem indexingItem = indexingItemRequest.toEntity();
		indexingItem = indexingItemRepository.save(indexingItem);
		return new IndexingItemResponse(indexingItem);
	}

	@Override
	@Transactional(readOnly = true)
	public List<IndexingItemResponse> findIndexingItemAfter(ZonedDateTime lastIndexedAt) {
		return indexingItemRepository.findByConsumedAtAfter(lastIndexedAt)
			.stream()
			.map(IndexingItemResponse::new)
			.collect(Collectors.toList());
	}
}
