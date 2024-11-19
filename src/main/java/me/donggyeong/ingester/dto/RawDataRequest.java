package me.donggyeong.ingester.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.donggyeong.ingester.domain.RawData;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RawDataRequest {
	private String action;
	private Map<String, Object> document;

	public RawData toEntity() {
		return RawData.builder()
			.action(this.action)
			.document(this.document)
			.isValid(this.document.containsKey("tenant") && this.document.containsKey("id"))
			.build();
	}
}
