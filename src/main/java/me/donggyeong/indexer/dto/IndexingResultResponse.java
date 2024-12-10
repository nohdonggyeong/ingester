package me.donggyeong.indexer.dto;

import java.time.ZonedDateTime;
import java.util.List;

import org.opensearch.client.opensearch.core.bulk.BulkResponseItem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import me.donggyeong.indexer.entity.IndexingResult;

@AllArgsConstructor
@Getter
@ToString
public class IndexingResultResponse {
	private Long took;
	private Boolean errors;
	private List<BulkResponseItem> items;
	private ZonedDateTime completedAt;

	public IndexingResultResponse(IndexingResult indexingResult) {
		this.took = indexingResult.getTook();
		this.errors = indexingResult.getErrors();
		this.items = indexingResult.getItems();
		this.completedAt = indexingResult.getCompletedAt();
	}
}
