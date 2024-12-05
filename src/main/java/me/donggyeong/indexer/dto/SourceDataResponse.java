package me.donggyeong.indexer.dto;

import java.time.ZonedDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.donggyeong.indexer.entity.SourceData;

@AllArgsConstructor
@Getter
public class SourceDataResponse {
	private Map<String, Object> data;
	private Boolean isValid;
	private ZonedDateTime consumedAt;

	public SourceDataResponse(SourceData sourceData) {
		this.data = sourceData.getData();
		this.isValid = sourceData.getIsValid();
		this.consumedAt = sourceData.getConsumedAt();
	}
}
