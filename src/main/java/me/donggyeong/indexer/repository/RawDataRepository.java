package me.donggyeong.indexer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.donggyeong.indexer.domain.RawData;

@Repository
public interface RawDataRepository extends JpaRepository<RawData, Long>, RawDataRepositoryCustom {
	RawData findFirstByOrderByIdDesc();
}
