package me.donggyeong.indexer.dto;

import java.util.Map;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.donggyeong.indexer.entity.ConsumedItem;
import me.donggyeong.indexer.enums.Action;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class ConsumedItemRequest {
	@NotNull(message = "'action' is required")
	private Action action;

	@NotNull(message = "'target' is required")
	private String target;

	@NotNull(message = "'docId' is required")
	private String docId;

	@NotNull(message = "'docBody' is required")
	private Map<String, Object> docBody;

	public ConsumedItem toEntity() {
		return ConsumedItem.builder()
			.action(this.action)
			.target(this.target)
			.docId(this.docId)
			.docBody(this.docBody)
			.build();
	}
}
