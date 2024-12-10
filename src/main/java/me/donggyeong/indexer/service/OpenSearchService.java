package me.donggyeong.indexer.service;

import java.util.List;

import org.opensearch.client.opensearch.cat.AliasesResponse;
import org.opensearch.client.opensearch.core.BulkResponse;
import org.opensearch.client.opensearch.indices.CreateIndexResponse;
import org.opensearch.client.opensearch.indices.DeleteIndexResponse;
import org.opensearch.client.transport.endpoints.BooleanResponse;

import me.donggyeong.indexer.dto.IndexingItemResponse;

public interface OpenSearchService {
	CreateIndexResponse createIndex(String indexName);
	BooleanResponse checkIndexExists(String indexName);
	DeleteIndexResponse deleteIndex(String indexName);
	BulkResponse requestBulk(List<IndexingItemResponse> dataList);
	AliasesResponse getAliasList();
}
