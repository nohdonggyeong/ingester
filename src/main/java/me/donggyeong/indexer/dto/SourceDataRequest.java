package me.donggyeong.indexer.dto;

import java.util.Map;

import org.springframework.util.ObjectUtils;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.donggyeong.indexer.entity.SourceData;
import me.donggyeong.indexer.enums.Action;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SourceDataRequest {
	@NotNull(message = "'action' is required.")
	private Action action;
	@NotNull(message = "'document' is required.")
	private Map<String, Object> document;

	public SourceData toEntity() {
		return SourceData.builder()
			.action(this.action)
			.document(this.document)
			.isValid(!ObjectUtils.isEmpty(this.document) && this.document.containsKey("source") && this.document.containsKey("id"))
			.build();
	}
}
