package me.donggyeong.indexer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.donggyeong.indexer.entity.IndexedItem;

@Repository
public interface IndexedItemRepository extends JpaRepository<IndexedItem, Long> {
}
