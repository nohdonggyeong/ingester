package me.donggyeong.indexer.dto;

import java.time.ZonedDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.donggyeong.indexer.entity.ConsumedItem;
import me.donggyeong.indexer.enums.Action;
import me.donggyeong.indexer.enums.IndexingState;

@AllArgsConstructor
@Getter
public class ConsumedItemResponse {
	private Long id;
	private Action action;
	private String target;
	private String docId;
	private Map<String, Object> docBody;
	private ZonedDateTime consumedAt;
	private IndexingState indexingState;

	public ConsumedItemResponse(ConsumedItem consumedItem) {
		this.id = consumedItem.getId();
		this.action = consumedItem.getAction();
		this.target = consumedItem.getTarget();
		this.docId = consumedItem.getDocId();
		this.docBody = consumedItem.getDocBody();
		this.consumedAt = consumedItem.getConsumedAt();
		this.indexingState = consumedItem.getIndexingState();
	}
}
