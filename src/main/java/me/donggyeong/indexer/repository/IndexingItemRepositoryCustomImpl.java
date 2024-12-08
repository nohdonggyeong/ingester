package me.donggyeong.indexer.repository;

import static me.donggyeong.indexer.entity.QIndexingItem.*;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import me.donggyeong.indexer.entity.IndexingItem;

@Repository
@RequiredArgsConstructor
public class IndexingItemRepositoryCustomImpl implements IndexingItemRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<IndexingItem> findByConsumedAtAfter(ZonedDateTime offset) {
		return jpaQueryFactory
			.selectFrom(indexingItem)
			.where(indexingItem.consumedAt.gt(offset))
			.fetch();
	}
}
