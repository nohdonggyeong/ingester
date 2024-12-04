package me.donggyeong.indexer.dto;

import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.donggyeong.indexer.entity.LatestIndices;

@AllArgsConstructor
@Getter
public class LatestIndicesResponse {
	private String source;
	private String latestIndex;
	private ZonedDateTime lastIndexedAt;
	private String indexAlias;

	public LatestIndicesResponse(LatestIndices latestIndices) {
		this.source = latestIndices.getSource();
		this.latestIndex = latestIndices.getLatestIndex();
		this.lastIndexedAt = latestIndices.getLastIndexedAt();
		this.indexAlias = latestIndices.getIndexAlias();
	}
}
