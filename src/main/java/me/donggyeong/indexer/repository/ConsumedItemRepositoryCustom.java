package me.donggyeong.indexer.repository;

import java.util.List;

import me.donggyeong.indexer.entity.ConsumedItem;
import me.donggyeong.indexer.enums.IndexingState;

public interface ConsumedItemRepositoryCustom {
	List<ConsumedItem> findByIndexingStateOrderByConsumedAt(IndexingState indexingState);
}
