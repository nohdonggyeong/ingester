package me.donggyeong.indexer.repository;

import static me.donggyeong.indexer.entity.QConsumedItem.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import me.donggyeong.indexer.entity.ConsumedItem;
import me.donggyeong.indexer.enums.IndexingState;

@Repository
@RequiredArgsConstructor
public class ConsumedItemRepositoryCustomImpl implements ConsumedItemRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<ConsumedItem> findByIndexingStateOrderByConsumedAt(IndexingState indexingState) {
		return jpaQueryFactory
			.selectFrom(consumedItem)
			.where(consumedItem.indexingState.eq(indexingState))
			.orderBy(consumedItem.consumedAt.asc())
			.fetch();
	}
}
