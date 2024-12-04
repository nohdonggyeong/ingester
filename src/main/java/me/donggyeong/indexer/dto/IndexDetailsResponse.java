package me.donggyeong.indexer.dto;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.donggyeong.indexer.entity.IndexDetails;

@AllArgsConstructor
@Getter
public class IndexDetailsResponse {
	private String source;
	private String indexAlias;
	private String latestIndex;
	private ZonedDateTime lastIndexedAt;

	public IndexDetailsResponse(IndexDetails indexDetails) {
		this.source = indexDetails.getSource();
		this.indexAlias = indexDetails.getIndexAlias();
		this.latestIndex = indexDetails.getLatestIndex();
		this.lastIndexedAt = indexDetails.getLastIndexedAt();
	}
}
