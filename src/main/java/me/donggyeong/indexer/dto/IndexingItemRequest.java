package me.donggyeong.indexer.dto;

import java.util.Map;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.donggyeong.indexer.entity.IndexingItem;
import me.donggyeong.indexer.enums.Action;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class IndexingItemRequest {
	@NotNull(message = "'action' is required")
	private Action action;

	@NotNull(message = "'targetName' is required")
	private String targetName;

	@NotNull(message = "'documentId' is required")
	private Long documentId;

	@NotNull(message = "'documentBody' is required")
	private Map<String, Object> documentBody;

	public IndexingItem toEntity() {
		return IndexingItem.builder()
			.action(this.action)
			.targetName(this.targetName)
			.documentId(this.documentId)
			.documentBody(this.documentBody)
			.build();
	}
}
