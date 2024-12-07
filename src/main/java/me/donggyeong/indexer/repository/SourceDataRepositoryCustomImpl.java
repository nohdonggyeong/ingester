package me.donggyeong.indexer.repository;

import static me.donggyeong.indexer.entity.QSourceData.*;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import me.donggyeong.indexer.entity.SourceData;

@Repository
@RequiredArgsConstructor
public class SourceDataRepositoryCustomImpl implements SourceDataRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<SourceData> findByConsumedAtAfter(ZonedDateTime offset) {
		return jpaQueryFactory
			.selectFrom(sourceData)
			.where(sourceData.consumedAt.gt(offset))
			.fetch();
	}
}
