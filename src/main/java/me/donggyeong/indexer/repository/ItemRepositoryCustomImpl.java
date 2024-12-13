package me.donggyeong.indexer.repository;

import static me.donggyeong.indexer.entity.QItem.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import me.donggyeong.indexer.entity.Item;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<Item> findByStatusIsNullOrderByConsumedAtAsc() {
		return jpaQueryFactory
			.selectFrom(item)
			.where(item.status.isNull())
			.orderBy(item.consumedAt.asc())
			.fetch();
	}
}
