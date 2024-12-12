package me.donggyeong.indexer.entity;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

import org.hibernate.annotations.Type;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.donggyeong.indexer.enums.Action;
import me.donggyeong.indexer.enums.IndexingState;

@Entity
@Table(name = "consumed_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ConsumedItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "action", nullable = false)
	@Enumerated(EnumType.STRING)
	private Action action;

	@Column(name = "target", nullable = false)
	private String target;

	@Column(name = "doc_id", nullable = false)
	private String docId;

	@Column(name = "doc_body", nullable = false, columnDefinition = "json")
	@Type(JsonType.class)
	private Map<String, Object> docBody;

	@Column(name = "consumed_at", nullable = false, updatable = false)
	private ZonedDateTime consumedAt;

	@Column(name = "indexing_state", nullable = false)
	@Enumerated(EnumType.STRING)
	private IndexingState indexingState;

	@PrePersist
	protected void onCreate() {
		this.consumedAt = ZonedDateTime.now(ZoneId.of("UTC"));
	}

	@Builder
	public ConsumedItem(Action action, String target, String docId, Map<String, Object> docBody) {
		this.action = action;
		this.target = target;
		this.docId = docId;
		this.docBody = docBody;
		this.indexingState = IndexingState.PENDING;
	}

	public void updateIndexingState(IndexingState indexingState) {
		this.indexingState = indexingState;
	}
}
