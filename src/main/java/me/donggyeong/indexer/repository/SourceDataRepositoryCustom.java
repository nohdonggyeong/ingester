package me.donggyeong.indexer.repository;

import java.time.ZonedDateTime;
import java.util.List;

import me.donggyeong.indexer.domain.SourceData;

public interface SourceDataRepositoryCustom {
	List<SourceData> findByCreatedAtAfterAndIsValidTrue(ZonedDateTime offset);
}
