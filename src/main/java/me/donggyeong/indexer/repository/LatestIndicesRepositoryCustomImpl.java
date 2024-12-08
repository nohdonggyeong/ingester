package me.donggyeong.indexer.repository;

import static me.donggyeong.indexer.entity.QLatestIndices.*;
import static me.donggyeong.indexer.entity.QSourceData.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class LatestIndicesRepositoryCustomImpl implements LatestIndicesRepositoryCustom{
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Optional<ZonedDateTime> findLatestByLastIndexedAt() {
		return Optional.ofNullable(
			jpaQueryFactory
				.select(latestIndices.lastIndexedAt)
				.from(latestIndices)
				.orderBy(latestIndices.lastIndexedAt.desc())
				.fetchFirst()
		);
	}
}
