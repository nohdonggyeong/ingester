package me.donggyeong.indexer.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.donggyeong.indexer.dto.LatestIndicesResponse;
import me.donggyeong.indexer.entity.LatestIndices;
import me.donggyeong.indexer.enums.ErrorCode;
import me.donggyeong.indexer.exception.CustomException;
import me.donggyeong.indexer.repository.LatestIndicesRepository;

@Service
@RequiredArgsConstructor
public class LatestIndicesServiceImpl implements LatestIndicesService {
	private final LatestIndicesRepository latestIndicesRepository;

	@Override
	@Transactional
	public LatestIndicesResponse createLatestIndex(String source) {
		LatestIndices latestIndices = LatestIndices.builder()
			.source(source)
			.build();
		latestIndices = latestIndicesRepository.save(latestIndices);
		return new LatestIndicesResponse(latestIndices);
	}

	@Override
	@Transactional(readOnly = true)
	public LatestIndicesResponse getLatestIndexBySource(String source) {
		Optional<LatestIndices> latestIndicesOptional = latestIndicesRepository.findBySource(source);
		return latestIndicesOptional.map(LatestIndicesResponse::new).orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public List<LatestIndicesResponse> getLatestIndices() {
		List<LatestIndices> latestIndicesList = latestIndicesRepository.findAll();
		return latestIndicesList.stream()
			.map(LatestIndicesResponse::new)
			.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public LatestIndicesResponse updateLatestIndex(String source) {
		Optional<LatestIndices> latestIndicesOptional = latestIndicesRepository.findBySource(source);
		if (latestIndicesOptional.isEmpty()) {
			throw new CustomException(ErrorCode.NOT_FOUND_LATEST_INDICES);
		}
		LatestIndices latestIndices = latestIndicesOptional.get();
		latestIndices.updateLatestIndex();
		latestIndices = latestIndicesRepository.save(latestIndices);
		return new LatestIndicesResponse(latestIndices);
	}

	@Override
	@Transactional
	public LatestIndicesResponse updateLastIndexedAt(String source) {
		Optional<LatestIndices> latestIndicesOptional = latestIndicesRepository.findBySource(source);
		if (latestIndicesOptional.isEmpty()) {
			throw new CustomException(ErrorCode.NOT_FOUND_LATEST_INDICES);
		}
		LatestIndices latestIndices = latestIndicesOptional.get();
		latestIndices.updateLastIndexedAt();
		return new LatestIndicesResponse(latestIndices);
	}

	@Override
	@Transactional(readOnly = true)
	public ZonedDateTime getLatestOrDefaultIndexedTime() {
		Optional<ZonedDateTime> optionalZonedDateTime = latestIndicesRepository.findLatestByLastIndexedAt();
		return optionalZonedDateTime.orElse(
			ZonedDateTime.of(
				1970, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC")
			)
		);
	}
}
