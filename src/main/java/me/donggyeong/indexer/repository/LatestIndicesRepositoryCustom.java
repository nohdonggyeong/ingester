package me.donggyeong.indexer.repository;

import java.time.ZonedDateTime;
import java.util.Optional;

public interface LatestIndicesRepositoryCustom {
	Optional<ZonedDateTime> findLatestByLastIndexedAt();
}
