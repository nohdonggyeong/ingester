package me.donggyeong.indexer.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.donggyeong.indexer.dto.IndexDetailsResponse;
import me.donggyeong.indexer.entity.IndexDetails;
import me.donggyeong.indexer.repository.IndexDetailsRepository;

@Service
@RequiredArgsConstructor
public class IndexDetailsServiceImpl implements IndexDetailsService{
	private final IndexDetailsRepository indexDetailsRepository;

	@Override
	@Transactional
	public IndexDetailsResponse createIndexDetails(String tenant) {
		IndexDetails indexDetails = IndexDetails.builder()
			.tenant(tenant)
			.build();
		indexDetails = indexDetailsRepository.save(indexDetails);
		return new IndexDetailsResponse(indexDetails);
	}

	@Override
	@Transactional(readOnly = true)
	public IndexDetailsResponse getIndexDetailsByTenant(String tenant) {
		Optional<IndexDetails> optionalIndexDetails = indexDetailsRepository.findByTenant(tenant);
		return optionalIndexDetails.map(IndexDetailsResponse::new).orElse(null);
	}

	@Override
	@Transactional
	public IndexDetailsResponse updateLastIndexedAt(String tenant) {
		Optional<IndexDetails> optionalIndexDetails = indexDetailsRepository.findByTenant(tenant);
		if (optionalIndexDetails.isEmpty()) {
			throw new IllegalArgumentException("not found: " + tenant);
		}
		IndexDetails indexDetails = optionalIndexDetails.get();
		indexDetails.updateLastIndexedAt();
		return new IndexDetailsResponse(indexDetails);
	}
}
