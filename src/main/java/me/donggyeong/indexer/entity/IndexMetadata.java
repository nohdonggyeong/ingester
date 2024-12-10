package me.donggyeong.indexer.entity;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

import org.hibernate.annotations.Type;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.donggyeong.indexer.enums.Status;

@Entity
@Table(name = "index_metadata")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class IndexMetadata {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "target_name", nullable = false)
	private String targetName;

	@Column(name = "index_name", nullable = false)
	private String indexName;

	@Column(name = "settings", columnDefinition = "json")
	@Type(JsonType.class)
	private Map<String, Object> settings;

	@Column(name = "mappings", columnDefinition = "json")
	@Type(JsonType.class)
	private Map<String, Object> mappings;

	@Column(name = "created_at", nullable = false, updatable = false)
	private ZonedDateTime createdAt;

	@Column(name = "is_write_index", nullable = false)
	private Boolean isWriteIndex;

	@Column(name = "status", nullable = false)
	private Status status;

	@Column(name = "alias_name")
	private String aliasName;

	@Column(name = "updated_at", nullable = false)
	private ZonedDateTime updatedAt;

	@PrePersist
	protected void onCreate() {
		this.createdAt = ZonedDateTime.now(ZoneId.of("UTC"));
		this.updatedAt = ZonedDateTime.now(ZoneId.of("UTC"));
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = ZonedDateTime.now(ZoneId.of("UTC"));
	}

	@Builder
	public IndexMetadata(String targetName, int count, Map<String, Object> settings, Map<String, Object> mappings) {
		this.targetName = targetName;
		this.indexName = "index_for_" + targetName + "_" + (count + 1);
		this.settings = settings;
		this.mappings = mappings;
		this.status = Status.OPEN;
		this.isWriteIndex = true;
		this.aliasName = "alias_for_" + targetName;
	}

	public void updateStatus(Status status) {
		this.status = status;
	}

	public void updateIsWriteIndex(Boolean isWriteIndex) {
		this.isWriteIndex = isWriteIndex;
	}
}
