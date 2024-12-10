package me.donggyeong.indexer.dto;

import java.util.Map;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.donggyeong.indexer.entity.IndexMetadata;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class IndexMetadataRequest {
	@NotNull(message = "'targetName' is required")
	private String targetName;

	@NotNull(message = "'settings' is required")
	private Map<String, Object> settings;

	@NotNull(message = "'mappings' is required")
	private Map<String, Object> mappings;

	private int targetCount;

	public IndexMetadata toEntity() {
		return IndexMetadata.builder()
			.targetName(this.targetName)
			.count(this.targetCount + 1)
			.settings(this.settings)
			.mappings(this.mappings)
			.build();
	}
}
