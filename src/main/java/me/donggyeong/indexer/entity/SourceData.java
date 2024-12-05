package me.donggyeong.indexer.entity;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

import org.hibernate.annotations.Type;
import org.springframework.util.ObjectUtils;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "source_data")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SourceData {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false)
	private Long id;

	@Column(name = "data", columnDefinition = "json")
	@Type(JsonType.class)
	private Map<String, Object> data;

	@Column(name = "is_valid", columnDefinition = "boolean default false", nullable = false)
	private Boolean isValid;

	@Column(name = "consumed_at", updatable = false)
	private ZonedDateTime consumedAt;

	@PrePersist
	protected void onCreate() {
		this.consumedAt = ZonedDateTime.now(ZoneId.of("UTC"));
	}

	@Builder
	public SourceData(Map<String, Object> data) {
		this.data = data;
		this.isValid = !ObjectUtils.isEmpty(this.data)
			&& this.data.containsKey("action")
			&& this.data.containsKey("source")
			&& this.data.containsKey("id");
	}
}
