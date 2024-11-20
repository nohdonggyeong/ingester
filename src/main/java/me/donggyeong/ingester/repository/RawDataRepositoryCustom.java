package me.donggyeong.ingester.repository;

import java.time.ZonedDateTime;
import java.util.List;

import me.donggyeong.ingester.domain.RawData;

public interface RawDataRepositoryCustom {
	List<RawData> findByCreatedAtAfterAndIsValidTrue(ZonedDateTime offset);
}
