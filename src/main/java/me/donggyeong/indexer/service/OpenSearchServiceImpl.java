package me.donggyeong.indexer.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.Refresh;
import org.opensearch.client.opensearch.cat.AliasesRequest;
import org.opensearch.client.opensearch.cat.AliasesResponse;
import org.opensearch.client.opensearch.core.BulkRequest;
import org.opensearch.client.opensearch.core.BulkResponse;
import org.opensearch.client.opensearch.core.bulk.BulkOperation;
import org.opensearch.client.opensearch.core.bulk.CreateOperation;
import org.opensearch.client.opensearch.core.bulk.DeleteOperation;
import org.opensearch.client.opensearch.core.bulk.IndexOperation;
import org.opensearch.client.opensearch.core.bulk.UpdateOperation;
import org.opensearch.client.opensearch.indices.Alias;
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
import me.donggyeong.indexer.dto.ConsumedItemResponse;
import me.donggyeong.indexer.enums.ErrorCode;
import me.donggyeong.indexer.exception.CustomException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class OpenSearchServiceImpl implements OpenSearchService{
	private final OpenSearchClient openSearchClient;

	@Override
	@Transactional
	public CreateIndexResponse createIndex(String target) {
		try {
			CreateIndexRequest createIndexRequest = new Builder()
				.index("index_for_" + target)
				.aliases("alias_for_" + target, new Alias.Builder()
					.isWriteIndex(true)
					.build())
				.build();

			return openSearchClient.indices().create(createIndexRequest);
		} catch (IOException e) {
			throw new CustomException(ErrorCode.OPENSEARCH_OPERATION_FAILED);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public BooleanResponse checkIndexExists(String index) {
		try {
			ExistsRequest existsRequest = ExistsRequest.of(r -> r.index(index));
			return openSearchClient.indices().exists(existsRequest);
		} catch (IOException e) {
			throw new CustomException(ErrorCode.OPENSEARCH_OPERATION_FAILED);
		}
	}

	@Override
	@Transactional
	public DeleteIndexResponse deleteIndex(String target) {
		try {
			DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest.Builder().index(target).build();
			return openSearchClient.indices().delete(deleteIndexRequest);
		} catch (IOException e) {
			throw new CustomException(ErrorCode.OPENSEARCH_OPERATION_FAILED);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public AliasesResponse findAllAliases() {
		try {
			AliasesRequest aliasesRequest = new AliasesRequest.Builder().sort("index").build();
			return openSearchClient.cat().aliases(aliasesRequest);
		} catch (IOException e) {
			throw new CustomException(ErrorCode.OPENSEARCH_OPERATION_FAILED);
		}
	}

	@Override
	@Transactional
	public BulkResponse requestBulkIndexing(List<ConsumedItemResponse> consumedItemResponseList) {
		try {
			List<BulkOperation> bulkOperationList = new ArrayList<>();

			for (ConsumedItemResponse consumedItemResponse : consumedItemResponseList) {
				String target = consumedItemResponse.getTarget();
				String aliasName = "alias_for_" + target;

				switch (consumedItemResponse.getAction()) {
					case INDEX:
						bulkOperationList.add(new BulkOperation.Builder().index(
							IndexOperation.of(io -> io
								.index(aliasName)
								.id(String.valueOf(consumedItemResponse.getDocId()))
								.document(consumedItemResponse.getDocBody())
							)
						).build());
						break;
					case CREATE:
						bulkOperationList.add(new BulkOperation.Builder().create(
							CreateOperation.of(io -> io
								.index(aliasName)
								.id(String.valueOf(consumedItemResponse.getDocId()))
								.document(consumedItemResponse.getDocBody())
							)
						).build());
						break;
					case UPDATE:
						bulkOperationList.add(new BulkOperation.Builder().update(
							UpdateOperation.of(io -> io
								.index(aliasName)
								.id(String.valueOf(consumedItemResponse.getDocId()))
								.document(consumedItemResponse.getDocBody())
							)
						).build());
						break;
					case DELETE:
						bulkOperationList.add(new BulkOperation.Builder().delete(
							DeleteOperation.of(io -> io
								.index(aliasName)
								.id(String.valueOf(consumedItemResponse.getDocId()))
							)
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

			BulkResponse bulkResponse = openSearchClient.bulk(bulkReq.build());

			bulkResponse.items().forEach(item -> {
				if (item.error() != null) {
					log.error("Error processing item with action={}, index={}, docId={}, error={}", item.operationType().jsonValue(), item.index(), item.id(), item.error());
				}
			});

			return bulkResponse;
		} catch (IOException e) {
			throw new CustomException(ErrorCode.OPENSEARCH_OPERATION_FAILED);
		}
	}
}
