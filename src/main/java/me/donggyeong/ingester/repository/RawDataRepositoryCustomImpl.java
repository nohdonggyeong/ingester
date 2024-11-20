package me.donggyeong.ingester.repository;

import static me.donggyeong.ingester.domain.QRawData.*;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import me.donggyeong.ingester.domain.RawData;

@Repository
@RequiredArgsConstructor
public class RawDataRepositoryCustomImpl implements RawDataRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<RawData> findByCreatedAtAfterAndIsValidTrue(ZonedDateTime offset) {
		return jpaQueryFactory
			.selectFrom(rawData)
			.where(rawData.createdAt.gt(offset)
				.and(rawData.isValid.isTrue())
			)
			.fetch();
	}
}
