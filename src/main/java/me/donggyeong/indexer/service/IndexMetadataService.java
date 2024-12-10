package me.donggyeong.indexer.service;

import java.time.ZonedDateTime;
import java.util.List;

import me.donggyeong.indexer.dto.IndexMetadataRequest;
import me.donggyeong.indexer.dto.IndexMetadataResponse;
import me.donggyeong.indexer.dto.LatestIndicesResponse;
import me.donggyeong.indexer.entity.IndexMetadata;

public interface IndexMetadataService {
	IndexMetadataResponse createIndexMetadata(String targetName, IndexMetadataRequest indexMetadataRequest);
	List<IndexMetadataResponse> getIndexMetadataListByTargetName(String targetName);
	IndexMetadataResponse getActiveIndexMetadataByTargetName(String targetName);
	IndexMetadataResponse updateIndexMetadata();

	LatestIndicesResponse createLatestIndex(String source);
	LatestIndicesResponse getLatestIndexBySource(String source);
	List<LatestIndicesResponse> getLatestIndices();
	LatestIndicesResponse updateLatestIndex(String source);
	LatestIndicesResponse updateLastIndexedAt(String source);
	ZonedDateTime getLatestOrDefaultIndexedTime();
}
