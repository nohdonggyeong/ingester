package me.donggyeong.indexer.repository;

import java.util.List;

import me.donggyeong.indexer.entity.Item;

public interface ItemRepositoryCustom {
	List<Item> findByStatusIsNullOrderByConsumedAtAsc();
}
