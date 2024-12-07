package me.donggyeong.indexer.dto;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.donggyeong.indexer.entity.LatestIndices;

@AllArgsConstructor
@Getter
public class LatestIndicesResponse {
	private String latestIndex;
	private ZonedDateTime lastIndexedAt;
	private String indexAlias;
	private String source;

	public LatestIndicesResponse(LatestIndices latestIndices) {
		this.latestIndex = latestIndices.getLatestIndex();
		this.lastIndexedAt = latestIndices.getLastIndexedAt();
		this.indexAlias = latestIndices.getIndexAlias();
		this.source = latestIndices.getSource();
	}
}
