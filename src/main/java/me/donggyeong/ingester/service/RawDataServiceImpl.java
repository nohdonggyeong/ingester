package me.donggyeong.ingester.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.donggyeong.ingester.domain.RawData;
import me.donggyeong.ingester.dto.RawDataRequest;
import me.donggyeong.ingester.dto.RawDataResponse;
import me.donggyeong.ingester.repository.RawDataRepository;

@Service
@RequiredArgsConstructor
public class RawDataServiceImpl implements RawDataService {

	private final RawDataRepository rawDataRepository;

	@Override
	public RawDataResponse createRawData(RawDataRequest rawDataRequest) {
		RawData rawData = rawDataRequest.toEntity();
		rawData = rawDataRepository.save(rawData);
		return new RawDataResponse(rawData);
	}

	@Override
	public List<RawDataResponse> getRawDataCandidateList() {
		return rawDataRepository.findByCreatedAtAfterAndIsValidTrue(ZonedDateTime.now().minusMinutes(1)).stream()
			.map(RawDataResponse::new)
			.collect(Collectors.toList());
	}
}
