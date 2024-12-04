package me.donggyeong.indexer.entity;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "latest_indices")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LatestIndices {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false)
	private Long id;

	@Column(name = "source", nullable = false)
	private String source;

	@Column(name = "latest_index")
	private String latestIndex;

	@Column(name = "last_indexed_at")
	private ZonedDateTime lastIndexedAt;

	@Column(name = "index_alias")
	private String indexAlias;

	@Builder
	public LatestIndices(String source) {
		this.source = source;
		this.indexAlias = "alias_of_" + source;
		updateLatestIndex();
	}

	public void updateLatestIndex() {
		ZonedDateTime utcNow = ZonedDateTime.now(ZoneId.of("UTC"));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSS");
		this.latestIndex = "index_of_" + this.source + "_" + utcNow.format(formatter);
	}

	public void updateLastIndexedAt() {
		this.lastIndexedAt = ZonedDateTime.now(ZoneId.of("UTC"));
	}
}
