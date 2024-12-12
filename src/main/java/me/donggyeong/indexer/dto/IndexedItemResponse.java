package me.donggyeong.indexer.dto;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import me.donggyeong.indexer.entity.IndexedItem;
import me.donggyeong.indexer.enums.Action;

@AllArgsConstructor
@Getter
@ToString
public class IndexedItemResponse {
	private Long id;
	private Action action;
	private String index;
	private String docId;
	private Integer status;
	private String result;
	private String error;
	private ZonedDateTime indexedAt;

	public IndexedItemResponse(IndexedItem indexedItem) {
		this.id = indexedItem.getId();
		this.action = indexedItem.getAction();
		this.index = indexedItem.getIndex();
		this.docId = indexedItem.getDocId();
		this.status = indexedItem.getStatus();
		this.result = indexedItem.getResult();
		this.error = indexedItem.getError();
		this.indexedAt = indexedItem.getIndexedAt();
	}
}
