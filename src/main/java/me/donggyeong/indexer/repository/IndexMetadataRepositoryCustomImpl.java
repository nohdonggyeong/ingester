package me.donggyeong.indexer.repository;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class IndexMetadataRepositoryCustomImpl implements IndexMetadataRepositoryCustom{
	private final JPAQueryFactory jpaQueryFactory;
}
