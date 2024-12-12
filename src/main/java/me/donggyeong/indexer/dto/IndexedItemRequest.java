package me.donggyeong.indexer.dto;

import org.opensearch.client.opensearch.core.bulk.BulkResponseItem;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.donggyeong.indexer.entity.IndexedItem;
import me.donggyeong.indexer.enums.Action;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class IndexedItemRequest {
	@NotNull(message = "'action' is required")
	private Action action;

	@NotNull(message = "'index' is required")
	private String index;

	@NotNull(message = "'docId' is required")
	private String docId;

	@NotNull(message = "'status' is required")
	private Integer status;

	private String result;

	private String error;

	public IndexedItem toEntity() {
		return IndexedItem.builder()
			.action(this.action)
			.index(this.index)
			.docId(this.docId)
			.status(this.status)
			.result(this.result)
			.error(this.error)
			.build();
	}

	public IndexedItemRequest(BulkResponseItem bulkResponseItem) {
		this.action = Action.of(bulkResponseItem.operationType().jsonValue());
		this.index = bulkResponseItem.index();
		this.docId = bulkResponseItem.id();
		this.status = bulkResponseItem.status();
		this.result = bulkResponseItem.result();
		this.error = bulkResponseItem.error() != null ? bulkResponseItem.error().type() : null;
	}
}
