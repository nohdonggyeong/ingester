package me.donggyeong.indexer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.donggyeong.indexer.entity.IndexingItem;

@Repository
public interface IndexingItemRepository extends JpaRepository<IndexingItem, Long>, IndexingItemRepositoryCustom {
}
