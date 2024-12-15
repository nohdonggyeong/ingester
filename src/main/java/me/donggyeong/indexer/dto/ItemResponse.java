package me.donggyeong.indexer.dto;

import java.time.ZonedDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import me.donggyeong.indexer.entity.Item;
import me.donggyeong.indexer.enums.Action;

@AllArgsConstructor
@Getter
@ToString
public class ItemResponse {
	private Long id;
	private Action action;
	private String target;
	private String docId;
	private Map<String, Object> docBody;
	private ZonedDateTime consumedAt;
	private String index;
	private Integer status;
	private String result;
	private String error;
	private ZonedDateTime indexedAt;

	public ItemResponse(Item item) {
		this.id = item.getId();
		this.action = item.getAction();
		this.target = item.getTarget();
		this.docId = item.getDocId();
		this.docBody = item.getDocBody();
		this.consumedAt = item.getConsumedAt();
		this.index = item.getIndex();
		this.status = item.getStatus();
		this.result = item.getResult();
		this.error = item.getError();
		this.indexedAt = item.getIndexedAt();
	}
}
