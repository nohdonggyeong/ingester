package me.donggyeong.indexer.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.donggyeong.indexer.entity.SourceData;
import me.donggyeong.indexer.dto.SourceDataRequest;
import me.donggyeong.indexer.dto.SourceDataResponse;
import me.donggyeong.indexer.repository.SourceDataRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SourceDataServiceImpl implements SourceDataService {

	private final SourceDataRepository sourceDataRepository;

	@Override
	@Transactional
	public SourceDataResponse createSourceData(SourceDataRequest sourceDataRequest) {
		SourceData sourceData = sourceDataRequest.toEntity();
		sourceData = sourceDataRepository.save(sourceData);
		return new SourceDataResponse(sourceData);
	}

	@Override
	public List<SourceDataResponse> getSourceDataAfterOffset(ZonedDateTime latestIndexedTime) {
		return sourceDataRepository.findByConsumedAtAfter(latestIndexedTime)
			.stream()
			.map(SourceDataResponse::new)
			.collect(Collectors.toList());
	}
}
