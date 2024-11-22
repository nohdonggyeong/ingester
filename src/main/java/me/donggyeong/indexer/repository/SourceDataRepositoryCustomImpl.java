package me.donggyeong.indexer.repository;

import static me.donggyeong.indexer.domain.QSourceData.*;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import me.donggyeong.indexer.domain.SourceData;

@Repository
@RequiredArgsConstructor
public class SourceDataRepositoryCustomImpl implements SourceDataRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<SourceData> findByCreatedAtAfterAndIsValidTrue(ZonedDateTime offset) {
		return jpaQueryFactory
			.selectFrom(sourceData)
			.where(sourceData.createdAt.gt(offset)
				.and(sourceData.isValid.isTrue())
			)
			.fetch();
	}
}
