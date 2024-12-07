package me.donggyeong.indexer.dto;

import java.util.Map;

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
	@NotNull(message = "'action' is required")
	private Action action;

	@NotNull(message = "'source' is required")
	private String source;

	@NotNull(message = "'dataId' is required")
	private Long dataId;

	@NotNull(message = "'data' is required")
	private Map<String, Object> data;

	public SourceData toEntity() {
		return SourceData.builder()
			.action(this.action)
			.source(this.source)
			.dataId(this.dataId)
			.data(this.data)
			.build();
	}
}
