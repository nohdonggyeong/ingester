package me.donggyeong.ingester.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.donggyeong.ingester.domain.RawData;

@Repository
public interface RawDataRepository extends JpaRepository<RawData, Long>, RawDataRepositoryCustom {
}
