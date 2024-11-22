package me.donggyeong.indexer.dto;

import java.util.Map;

import org.springframework.util.ObjectUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.donggyeong.indexer.domain.RawData;
import me.donggyeong.indexer.enums.ActionEnum;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RawDataRequest {
	private ActionEnum action;
	private Map<String, Object> document;

	public RawData toEntity() {
		return RawData.builder()
			.action(this.action)
			.document(this.document)
			.isValid(!ObjectUtils.isEmpty(this.document) && this.document.containsKey("tenant") && this.document.containsKey("id"))
			.build();
	}
}
