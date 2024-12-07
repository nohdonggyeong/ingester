package me.donggyeong.indexer.dto;

import java.time.ZonedDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.donggyeong.indexer.entity.SourceData;
import me.donggyeong.indexer.enums.Action;

@AllArgsConstructor
@Getter
public class SourceDataResponse {
	private Action action;
	private String source;
	private Long dataId;
	private Map<String, Object> data;
	private ZonedDateTime consumedAt;

	public SourceDataResponse(SourceData sourceData) {
		this.action = sourceData.getAction();
		this.source = sourceData.getSource();
		this.dataId = sourceData.getDataId();
		this.data = sourceData.getData();
		this.consumedAt = sourceData.getConsumedAt();
	}
}
