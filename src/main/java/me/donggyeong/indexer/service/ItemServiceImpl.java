package me.donggyeong.indexer.service;

import java.time.ZonedDateTime;
import java.util.List;

import org.opensearch.client.opensearch.core.bulk.BulkResponseItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.donggyeong.indexer.dto.ItemRequest;
import me.donggyeong.indexer.entity.Item;
import me.donggyeong.indexer.dto.ItemResponse;
import me.donggyeong.indexer.enums.ErrorCode;
import me.donggyeong.indexer.exception.CustomException;
import me.donggyeong.indexer.repository.ItemRepository;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
	private final ItemRepository itemRepository;

	@Override
	@Transactional
	public ItemResponse save(ItemRequest itemRequest) {
		Item item = itemRequest.toEntity();
		item = itemRepository.save(item);
		return new ItemResponse(item);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ItemResponse> findByStatusIsNullOrderByConsumedAtAsc() {
		return itemRepository.findByStatusIsNullOrderByConsumedAtAsc().stream()
			.map(ItemResponse::new).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<ItemResponse> findAll() {
		return itemRepository.findAll().stream().map(ItemResponse::new).toList();
	}

	@Transactional
	public ItemResponse update(Long id, BulkResponseItem bulkResponseItem, ZonedDateTime utcNow) {
		Item item = itemRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.ITEM_NOT_FOUND));
		item.update(bulkResponseItem.index(), bulkResponseItem.status(), bulkResponseItem.result(),
			bulkResponseItem.error() != null ? bulkResponseItem.error().type() : null, utcNow);
		item = itemRepository.save(item);
		return new ItemResponse(item);
	}
}
