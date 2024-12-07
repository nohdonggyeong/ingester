package me.donggyeong.indexer.repository;

import java.time.ZonedDateTime;
import java.util.List;

import me.donggyeong.indexer.entity.SourceData;

public interface SourceDataRepositoryCustom {
	List<SourceData> findByConsumedAtAfter(ZonedDateTime offset);
}
