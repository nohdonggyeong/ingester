package me.donggyeong.ingester.dto;

import java.time.ZonedDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.donggyeong.ingester.domain.RawData;

@AllArgsConstructor
@Getter
public class RawDataResponse {
	private String action;
	private Map<String, Object> document;
	private Boolean isValid;
	private ZonedDateTime createdAt;

	public RawDataResponse(RawData rawData) {
		this.action = rawData.getAction();
		this.document = rawData.getDocument();
		this.isValid = rawData.getIsValid();
		this.createdAt = rawData.getCreatedAt();
	}
}
