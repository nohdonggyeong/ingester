package me.donggyeong.indexer.repository;

import java.time.ZonedDateTime;
import java.util.List;

import me.donggyeong.indexer.domain.RawData;

public interface RawDataRepositoryCustom {
	List<RawData> findByCreatedAtAfterAndIsValidTrue(ZonedDateTime offset);
}
