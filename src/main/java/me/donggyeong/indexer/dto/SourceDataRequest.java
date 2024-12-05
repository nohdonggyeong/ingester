package me.donggyeong.indexer.dto;

import java.util.Map;

import org.springframework.util.ObjectUtils;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.donggyeong.indexer.entity.SourceData;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SourceDataRequest {
	@NotNull(message = "'data' is required.")
	private Map<String, Object> data;

	public SourceData toEntity() {
		return SourceData.builder()
			.data(this.data)
			.build();
	}
}
