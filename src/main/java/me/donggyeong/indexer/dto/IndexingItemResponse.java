package me.donggyeong.indexer.dto;

import java.time.ZonedDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.donggyeong.indexer.entity.IndexingItem;
import me.donggyeong.indexer.enums.Action;

@AllArgsConstructor
@Getter
public class IndexingItemResponse {
	private Action action;
	private String targetName;
	private Long documentId;
	private Map<String, Object> documentBody;
	private ZonedDateTime consumedAt;

	public IndexingItemResponse(IndexingItem indexingItem) {
		this.action = indexingItem.getAction();
		this.targetName = indexingItem.getTargetName();
		this.documentId = indexingItem.getDocumentId();
		this.documentBody = indexingItem.getDocumentBody();
		this.consumedAt = indexingItem.getConsumedAt();
	}
}
