package me.donggyeong.indexer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.donggyeong.indexer.domain.SourceData;

@Repository
public interface SourceDataRepository extends JpaRepository<SourceData, Long>, SourceDataRepositoryCustom {
	SourceData findFirstByOrderByIdDesc();
}
