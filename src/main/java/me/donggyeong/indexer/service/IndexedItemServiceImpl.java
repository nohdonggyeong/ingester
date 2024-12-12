package me.donggyeong.indexer.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.donggyeong.indexer.dto.IndexedItemRequest;
import me.donggyeong.indexer.dto.IndexedItemResponse;
import me.donggyeong.indexer.entity.IndexedItem;
import me.donggyeong.indexer.repository.IndexedItemRepository;

@Service
@RequiredArgsConstructor
public class IndexedItemServiceImpl implements IndexedItemService {
	private final IndexedItemRepository indexedItemRepository;

	@Override
	@Transactional
	public IndexedItemResponse save(IndexedItemRequest indexedItemRequest) {
		IndexedItem indexedItem = indexedItemRequest.toEntity();
		indexedItem = indexedItemRepository.save(indexedItem);
		return new IndexedItemResponse(indexedItem);
	}
}
