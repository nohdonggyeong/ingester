package me.donggyeong.indexer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.donggyeong.indexer.entity.IndexDetails;

@Repository
public interface IndexDetailsRepository extends JpaRepository<IndexDetails, Long> {
	Optional<IndexDetails> findBySource(String source);
}
