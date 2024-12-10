package me.donggyeong.indexer.dto;

import java.time.ZonedDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.donggyeong.indexer.entity.IndexMetadata;
import me.donggyeong.indexer.enums.Status;

@AllArgsConstructor
@Getter
public class IndexMetadataResponse {
	private String targetName;
	private String indexName;
	private Map<String, Object> settings;
	private Map<String, Object> mappings;
	private ZonedDateTime createdAt;
	private Boolean isWriteIndex;
	private Status status;
	private String aliasName;
	private ZonedDateTime updatedAt;

	public IndexMetadataResponse(IndexMetadata indexMetadata) {
		this.targetName = indexMetadata.getTargetName();
		this.indexName = indexMetadata.getIndexName();
		this.settings = indexMetadata.getSettings();
		this.mappings = indexMetadata.getMappings();
		this.createdAt = indexMetadata.getCreatedAt();
		this.isWriteIndex = indexMetadata.getIsWriteIndex();
		this.status = indexMetadata.getStatus();
		this.aliasName = indexMetadata.getAliasName();
		this.updatedAt = indexMetadata.getUpdatedAt();
	}
}
