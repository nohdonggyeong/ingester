package me.donggyeong.indexer.entity;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
import me.donggyeong.indexer.enums.Status;

@Entity
@Table(name = "indexing_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class IndexingItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "action", nullable = false)
	@Enumerated(EnumType.STRING)
	private Action action;

	@Column(name = "target_name", nullable = false)
	private String targetName;

	@Column(name = "document_id", nullable = false)
	private Long documentId;

	@Column(name = "document_body", nullable = false, columnDefinition = "json")
	@Type(JsonType.class)
	private Map<String, Object> documentBody;

	@Column(name = "consumed_at", nullable = false, updatable = false)
	private ZonedDateTime consumedAt;

	@PrePersist
	protected void onCreate() {
		this.consumedAt = ZonedDateTime.now(ZoneId.of("UTC"));
	}

	@Builder
	public IndexingItem(Action action, String targetName, Long documentId, Map<String, Object> documentBody) {
		this.action = action;
		this.targetName = targetName;
		this.documentId = documentId;
		this.documentBody = documentBody;
	}
}
