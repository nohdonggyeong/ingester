package me.donggyeong.indexer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.donggyeong.indexer.entity.LatestIndices;

@Repository
public interface LatestIndicesRepository extends JpaRepository<LatestIndices, Long>, LatestIndicesRepositoryCustom {
	Optional<LatestIndices> findBySource(String source);
}
