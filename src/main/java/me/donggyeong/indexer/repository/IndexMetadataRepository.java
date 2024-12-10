package me.donggyeong.indexer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.donggyeong.indexer.entity.IndexMetadata;

@Repository
public interface IndexMetadataRepository extends JpaRepository<IndexMetadata, Long>, IndexMetadataRepositoryCustom {
	Optional<IndexMetadata> findByTargetName(String targetName);
}
