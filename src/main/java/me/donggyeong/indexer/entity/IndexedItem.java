package me.donggyeong.indexer.entity;

import java.time.ZoneId;
import java.time.ZonedDateTime;

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
@Table(name = "indexed_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class IndexedItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "action", nullable = false)
	@Enumerated(EnumType.STRING)
	private Action action;

	@Column(name = "index", nullable = false)
	private String index;

	@Column(name = "doc_id", nullable = false)
	private String docId;

	@Column(name = "status", nullable = false)
	private Integer status;

	@Column(name = "result")
	private String result;

	@Column(name = "error")
	private String error;

	@Column(name = "indexed_at", nullable = false, updatable = false)
	private ZonedDateTime indexedAt;

	@PrePersist
	protected void onCreate() {
		this.indexedAt = ZonedDateTime.now(ZoneId.of("UTC"));
	}

	@Builder
	public IndexedItem(Action action, String index, String docId, Integer status, String result, String error) {
		this.action = action;
		this.index = index;
		this.docId = docId;
		this.status = status;
		this.result = result;
		this.error = error;
	}
}
