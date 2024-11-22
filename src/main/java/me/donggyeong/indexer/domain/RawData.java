package me.donggyeong.indexer.domain;

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
import me.donggyeong.indexer.enums.ActionEnum;

@Entity
@Table(name = "raw_data")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RawData {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false)
	private Long id;

	@Column(name = "action", nullable = false)
	@Enumerated(EnumType.STRING)
	private ActionEnum action;

	@Column(name = "document", columnDefinition = "json")
	@Type(JsonType.class)
	private Map<String, Object> document;

	@Column(name = "is_valid", columnDefinition = "boolean default false", nullable = false)
	private Boolean isValid;

	@Column(name = "created_at", updatable = false)
	private ZonedDateTime createdAt;

	@PrePersist
	protected void onCreate() {
		if (createdAt == null) {
			createdAt = ZonedDateTime.now();
		}
	}

	@Builder
	public RawData(ActionEnum action, Map<String, Object> document, Boolean isValid) {
		this.action = action;
		this.document = document;
		this.isValid = isValid;
	}

	public void update(ZonedDateTime createdAt) {
		this.createdAt = createdAt;
	}
}
