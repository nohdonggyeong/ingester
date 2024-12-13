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
@Table(name = "item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Item {
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

	@Column(name = "index")
	private String index;

	@Column(name = "status")
	private Integer status;

	@Column(name = "result")
	private String result;

	@Column(name = "error")
	private String error;

	@Column(name = "indexed_at")
	private ZonedDateTime indexedAt;

	@PrePersist
	protected void onCreate() {
		this.consumedAt = ZonedDateTime.now(ZoneId.of("UTC"));
	}

	@Builder
	public Item(Action action, String target, String docId, Map<String, Object> docBody) {
		this.action = action;
		this.target = target;
		this.docId = docId;
		this.docBody = docBody;
	}

	public void update(String index, Integer status, String result, String error, ZonedDateTime indexedAt) {
		this.index = index;
		this.status = status;
		this.result = result;
		this.error = error;
		this.indexedAt = indexedAt;
	}
}
