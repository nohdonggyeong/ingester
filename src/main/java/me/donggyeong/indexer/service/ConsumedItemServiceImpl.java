package me.donggyeong.indexer.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.donggyeong.indexer.dto.ConsumedItemRequest;
import me.donggyeong.indexer.entity.ConsumedItem;
import me.donggyeong.indexer.dto.ConsumedItemResponse;
import me.donggyeong.indexer.enums.ErrorCode;
import me.donggyeong.indexer.enums.IndexingState;
import me.donggyeong.indexer.exception.CustomException;
import me.donggyeong.indexer.repository.ConsumedItemRepository;

@Service
@RequiredArgsConstructor
public class ConsumedItemServiceImpl implements ConsumedItemService {
	private final ConsumedItemRepository consumedItemRepository;

	@Override
	@Transactional
	public ConsumedItemResponse save(ConsumedItemRequest consumedItemRequest) {
		ConsumedItem consumedItem = consumedItemRequest.toEntity();
		consumedItem = consumedItemRepository.save(consumedItem);
		return new ConsumedItemResponse(consumedItem);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ConsumedItemResponse> findByIndexingStateOrderByConsumedAt(IndexingState indexingState) {
		return consumedItemRepository.findByIndexingStateOrderByConsumedAt(indexingState)
			.stream()
			.map(ConsumedItemResponse::new)
			.collect(Collectors.toList());
	}

	@Transactional
	public void updateIndexingStatus(ConsumedItemResponse consumedItemResponse, IndexingState indexingState) {
		ConsumedItem consumedItem = consumedItemRepository.findById(consumedItemResponse.getId())
			.orElseThrow(() -> new CustomException(ErrorCode.CONSUMED_ITEM_NOT_FOUND));
		consumedItem.updateIndexingState(indexingState);
		consumedItemRepository.save(consumedItem);
	}
}
