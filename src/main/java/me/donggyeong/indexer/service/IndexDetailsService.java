package me.donggyeong.indexer.service;

import java.util.List;
import java.util.Optional;

import me.donggyeong.indexer.dto.IndexDetailsResponse;
import me.donggyeong.indexer.entity.IndexDetails;

public interface IndexDetailsService {
	IndexDetailsResponse createIndexDetails(String tenant);
	IndexDetailsResponse getIndexDetailsByTenant(String tenant);
	IndexDetailsResponse updateLastIndexedAt(String tenant);
}