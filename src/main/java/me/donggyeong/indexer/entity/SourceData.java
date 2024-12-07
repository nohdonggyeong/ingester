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

@Entity
@Table(name = "source_data")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SourceData {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "action", nullable = false)
	@Enumerated(EnumType.STRING)
	private Action action;

	@Column(name = "source", nullable = false)
	private String source;

	@Column(name = "data_id", nullable = false)
	private Long dataId;

	@Column(name = "data", columnDefinition = "json", nullable = false)
	@Type(JsonType.class)
	private Map<String, Object> data;

	@Column(name = "consumed_at", updatable = false)
	private ZonedDateTime consumedAt;

	@PrePersist
	protected void onCreate() {
		this.consumedAt = ZonedDateTime.now(ZoneId.of("UTC"));
	}

	@Builder
	public SourceData(Action action, String source, Long dataId, Map<String, Object> data) {
		this.action = action;
		this.source = source;
		this.dataId = dataId;
		this.data = data;
	}
}
