package me.donggyeong.indexer.entity;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import org.hibernate.annotations.Type;
import org.opensearch.client.opensearch.core.BulkResponse;
import org.opensearch.client.opensearch.core.bulk.BulkResponseItem;

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
@Table(name = "indexing_result")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class IndexingResult {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "took", nullable = false)
	private Long took;

	@Column(name = "errors", nullable = false)
	private Boolean errors;

	@Column(name = "items", nullable = false, columnDefinition = "json")
	@Type(JsonType.class)
	private List<BulkResponseItem> items;

	@Column(name = "completed_at", nullable = false, updatable = false)
	private ZonedDateTime completedAt;

	@PrePersist
	protected void onCreate() {
		this.completedAt = ZonedDateTime.now(ZoneId.of("UTC"));
	}

	@Builder
	public IndexingResult(Long took, Boolean errors, List<BulkResponseItem> items) {
		this.took = took;
		this.errors = errors;
		this.items = items;
	}
}
