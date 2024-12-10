package me.donggyeong.indexer.dto;

import java.util.List;

import org.opensearch.client.opensearch.core.BulkResponse;
import org.opensearch.client.opensearch.core.bulk.BulkResponseItem;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.donggyeong.indexer.entity.IndexingResult;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class IndexingResultRequest {
	@NotNull(message = "'took' is required")
	private Long took;

	@NotNull(message = "'errors' is required")
	private Boolean errors;

	@NotNull(message = "'items' is required")
	private List<BulkResponseItem> items;

	public IndexingResult toEntity() {
		return IndexingResult.builder()
			.took(this.took)
			.errors(this.errors)
			.items(this.items)
			.build();
	}

	public IndexingResultRequest(BulkResponse bulkResponse) {
		this.took = bulkResponse.took();
		this.errors = bulkResponse.errors();
		this.items = bulkResponse.items();
	}
}
