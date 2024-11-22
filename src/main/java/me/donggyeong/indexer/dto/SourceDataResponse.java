package me.donggyeong.indexer.dto;

import java.time.ZonedDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.donggyeong.indexer.domain.SourceData;
import me.donggyeong.indexer.enumType.Action;

@AllArgsConstructor
@Getter
public class SourceDataResponse {
	private Action action;
	private Map<String, Object> document;
	private Boolean isValid;
	private ZonedDateTime createdAt;

	public SourceDataResponse(SourceData sourceData) {
		this.action = sourceData.getAction();
		this.document = sourceData.getDocument();
		this.isValid = sourceData.getIsValid();
		this.createdAt = sourceData.getCreatedAt();
	}
}
