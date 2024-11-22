package me.donggyeong.indexer.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.donggyeong.indexer.domain.RawData;
import me.donggyeong.indexer.dto.RawDataRequest;
import me.donggyeong.indexer.dto.RawDataResponse;
import me.donggyeong.indexer.repository.RawDataRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RawDataServiceImpl implements RawDataService {

	private final RawDataRepository rawDataRepository;

	@Override
	@Transactional
	public RawDataResponse createRawData(RawDataRequest rawDataRequest) {
		RawData rawData = rawDataRequest.toEntity();
		rawData = rawDataRepository.save(rawData);
		return new RawDataResponse(rawData);
	}

	@Override
	public List<RawDataResponse> getRawDataAfterOffset() {
		// TODO: Modify offset to lastBulkRequestedTime
		return rawDataRepository.findByCreatedAtAfterAndIsValidTrue(ZonedDateTime.now().minusMinutes(1))
			.stream()
			.map(RawDataResponse::new)
			.collect(Collectors.toList());
	}
}
