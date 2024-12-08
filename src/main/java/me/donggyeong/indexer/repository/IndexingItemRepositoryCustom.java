package me.donggyeong.indexer.repository;

import java.time.ZonedDateTime;
import java.util.List;

import me.donggyeong.indexer.entity.IndexingItem;

public interface IndexingItemRepositoryCustom {
	List<IndexingItem> findByConsumedAtAfter(ZonedDateTime offset);
}
