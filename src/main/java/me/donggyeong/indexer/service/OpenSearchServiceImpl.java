package me.donggyeong.indexer.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.Refresh;
import org.opensearch.client.opensearch.core.BulkRequest;
import org.opensearch.client.opensearch.core.BulkResponse;
import org.opensearch.client.opensearch.core.bulk.BulkOperation;
import org.opensearch.client.opensearch.core.bulk.CreateOperation;
import org.opensearch.client.opensearch.core.bulk.DeleteOperation;
import org.opensearch.client.opensearch.core.bulk.IndexOperation;
import org.opensearch.client.opensearch.core.bulk.UpdateOperation;
import org.opensearch.client.opensearch.indices.CreateIndexRequest;
import org.opensearch.client.opensearch.indices.CreateIndexRequest.Builder;
import org.opensearch.client.opensearch.indices.CreateIndexResponse;
import org.opensearch.client.opensearch.indices.DeleteIndexRequest;
import org.opensearch.client.opensearch.indices.DeleteIndexResponse;
import org.opensearch.client.opensearch.indices.ExistsRequest;
import org.opensearch.client.transport.endpoints.BooleanResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.donggyeong.indexer.enums.Action;
import me.donggyeong.indexer.enums.ErrorCode;
import me.donggyeong.indexer.exception.CustomException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class OpenSearchServiceImpl implements OpenSearchService{
	private final OpenSearchClient openSearchClient;
	private final LatestIndicesService latestIndicesService;

	@Override
	@Transactional
	public CreateIndexResponse createIndex(String index) {
		try {
			CreateIndexRequest createIndexRequest = new Builder()
				.index(index)
				.build();
			return openSearchClient.indices().create(createIndexRequest);
		} catch (IOException e) {
			throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public BooleanResponse checkIndexExists(String indexName) {
		try {
			ExistsRequest existsRequest = ExistsRequest.of(r -> r.index(indexName));
			return openSearchClient.indices().exists(existsRequest);
		} catch (IOException e) {
			throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@Transactional
	public DeleteIndexResponse deleteIndex(String indexName) {
		try {
			DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest.Builder().index(indexName).build();
			return openSearchClient.indices().delete(deleteIndexRequest);
		} catch (IOException e) {
			throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@Transactional
	public BulkResponse requestBulk(List<Map<String, Object>> dataList) {
		try {
			List<BulkOperation> bulkOperationList = new ArrayList<>();

			for (Map<String, Object> data : dataList) {
				Action action = Action.of(String.valueOf(data.get("action")));
				String index = String.valueOf(data.get("index"));
				String id = String.valueOf(data.get("id"));

				switch (action) {
					case INDEX:
						bulkOperationList.add(new BulkOperation.Builder().index(
							IndexOperation.of(io -> io.index(index).id(id).document(data))
						).build());
						break;
					case CREATE:
						bulkOperationList.add(new BulkOperation.Builder().create(
							CreateOperation.of(io -> io.index(index).id(id).document(data))
						).build());
						break;
					case UPDATE:
						bulkOperationList.add(new BulkOperation.Builder().update(
							UpdateOperation.of(io -> io.index(index).id(id).document(data))
						).build());
						break;
					case DELETE:
						bulkOperationList.add(new BulkOperation.Builder().delete(
							DeleteOperation.of(io -> io.index(index).id(id))
						).build());
						break;
					default:
						log.error("This action is not supported");
						break;
				}
			}

			BulkRequest.Builder bulkReq = new BulkRequest.Builder()
				.operations(bulkOperationList)
				.refresh(Refresh.WaitFor);

			BulkResponse response = openSearchClient.bulk(bulkReq.build());

			response.items().forEach(item -> {
				if (item.error() != null) {
					log.error("Error processing item with ID {}: {}", item.id(), item.error());
				}
			});

			return response;
		} catch (IOException e) {
			throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

}
