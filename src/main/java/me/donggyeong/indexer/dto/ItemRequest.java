package me.donggyeong.indexer.dto;

import java.util.Map;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import me.donggyeong.indexer.entity.Item;
import me.donggyeong.indexer.enums.Action;

@NoArgsConstructor
@Setter
@Getter
@ToString
public class ItemRequest {
	@NotNull(message = "'action' is required")
	private Action action;

	@NotNull(message = "'target' is required")
	private String target;

	@NotNull(message = "'docId' is required")
	private String docId;

	@NotNull(message = "'docBody' is required")
	private Map<String, Object> docBody;

	public Item toEntity() {
		return Item.builder()
			.action(this.action)
			.target(this.target)
			.docId(this.docId)
			.docBody(this.docBody)
			.build();
	}
}
