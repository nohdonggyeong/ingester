package me.donggyeong.indexer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.donggyeong.indexer.entity.IndexingResult;

@Repository
public interface IndexingResultRepository extends JpaRepository<IndexingResult, Long> {
}
