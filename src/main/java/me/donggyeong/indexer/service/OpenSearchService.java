package me.donggyeong.indexer.service;

import java.util.List;

import org.opensearch.client.opensearch.core.BulkResponse;
import org.opensearch.client.opensearch.indices.CreateIndexResponse;
import org.opensearch.client.opensearch.indices.DeleteIndexResponse;
import org.opensearch.client.transport.endpoints.BooleanResponse;

import me.donggyeong.indexer.dto.ItemResponse;

public interface OpenSearchService {
	CreateIndexResponse createIndexWithAlias(String target);
	BulkResponse requestBulkIndexing(List<ItemResponse> itemResponseList);
	BooleanResponse checkIndexExists(String index);
	List<String> findAllAliases();
	DeleteIndexResponse deleteIndex(String target);
}
