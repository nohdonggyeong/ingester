package me.donggyeong.indexer.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.Refresh;
import org.opensearch.client.opensearch._types.mapping.DynamicTemplate;
import org.opensearch.client.opensearch._types.mapping.IntegerNumberProperty;
import org.opensearch.client.opensearch._types.mapping.Property;
import org.opensearch.client.opensearch._types.mapping.TextProperty;
import org.opensearch.client.opensearch._types.mapping.TypeMapping;
import org.opensearch.client.opensearch.cat.AliasesRequest;
import org.opensearch.client.opensearch.cat.AliasesResponse;
import org.opensearch.client.opensearch.cat.aliases.AliasesRecord;
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
import org.opensearch.client.opensearch.indices.IndexSettings;
import org.opensearch.client.transport.endpoints.BooleanResponse;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.donggyeong.indexer.dto.ItemResponse;
import me.donggyeong.indexer.enums.Action;
import me.donggyeong.indexer.enums.ErrorCode;
import me.donggyeong.indexer.exception.CustomException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class OpenSearchServiceImpl implements OpenSearchService{
	private final OpenSearchClient openSearchClient;

	private static final String PREFIX_INDEX = "index_for_";
	private static final String PREFIX_ALIAS = "alias_for_";
	private static final String PREFIX_DOT = ".";
	private static final String PREFIX_SECURITY = "security-";

	@Override
	@Transactional
	@CacheEvict(value = "aliasesCache", allEntries = true)
	public CreateIndexResponse createIndexWithAlias(String target) {
		try {
			IndexSettings settings = new IndexSettings.Builder()
				.numberOfShards("3")
				.numberOfReplicas("1")
				.build();

			TypeMapping mappings = new TypeMapping.Builder()
				.dynamicTemplates(
					List.of(
						Map.of("nori_text",
							new DynamicTemplate.Builder()
								.match("*")
								.matchMappingType("string")
								.mapping(new Property.Builder()
									.text(new TextProperty.Builder()
										.analyzer("nori")
										.build())
									.build())
								.build()
						)
					)
				)
				.build();

			CreateIndexRequest createIndexRequest = new Builder()
				.index(PREFIX_INDEX + target)
				.aliases(PREFIX_ALIAS + target, new Alias.Builder()
					.isWriteIndex(true)
					.build()
				)
				.settings(settings)
				.mappings(mappings)
				.build();

			return openSearchClient.indices().create(createIndexRequest);
		} catch (IOException e) {
			throw new CustomException(ErrorCode.OPENSEARCH_OPERATION_FAILED);
		}
	}

	@Override
	@Transactional
	public BulkResponse requestBulkIndexing(List<ItemResponse> itemResponseList) {
		try {
			List<BulkOperation> bulkOperationList = new ArrayList<>();

			for (ItemResponse itemResponse : itemResponseList) {
				String target = itemResponse.getTarget();
				String aliasName = PREFIX_ALIAS + target;

				List<String> existingAliases = findAllAliases();
				if (!existingAliases.contains(aliasName)) {
					createIndexWithAlias(target);
				}

				switch (itemResponse.getAction()) {
					case INDEX:
						bulkOperationList.add(new BulkOperation.Builder().index(
							IndexOperation.of(io -> io
								.index(aliasName)
								.id(String.valueOf(itemResponse.getDocId()))
								.document(itemResponse.getDocBody())
							)
						).build());
						break;
					case CREATE:
						bulkOperationList.add(new BulkOperation.Builder().create(
							CreateOperation.of(io -> io
								.index(aliasName)
								.id(String.valueOf(itemResponse.getDocId()))
								.document(itemResponse.getDocBody())
							)
						).build());
						break;
					case UPDATE:
						bulkOperationList.add(new BulkOperation.Builder().update(
							UpdateOperation.of(io -> io
								.index(aliasName)
								.id(String.valueOf(itemResponse.getDocId()))
								.document(itemResponse.getDocBody())
							)
						).build());
						break;
					case DELETE:
						bulkOperationList.add(new BulkOperation.Builder().delete(
							DeleteOperation.of(io -> io
								.index(aliasName)
								.id(String.valueOf(itemResponse.getDocId()))
							)
						).build());
						break;
					default:
						throw new CustomException(ErrorCode.ACTION_NOT_SUPPORTED);
				}
			}

			BulkRequest.Builder bulkReq = new BulkRequest.Builder()
				.operations(bulkOperationList)
				.refresh(Refresh.WaitFor);

			BulkResponse bulkResponse = openSearchClient.bulk(bulkReq.build());

			bulkResponse.items().forEach(item -> {
				if (item.error() != null) {
					log.error("[ Error ] with {action={}, index={}, docId={}, error={}}", Action.of(item.operationType().jsonValue()), item.index(), item.id(), item.error().type());
				}
			});

			return bulkResponse;
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
	@Transactional(readOnly = true)
	@Cacheable(value = "aliasesCache", unless = "#result.isEmpty()")
	public List<String> findAllAliases() {
		try {
			AliasesRequest aliasesRequest = new AliasesRequest.Builder().sort("index").build();
			AliasesResponse aliasesResponse = openSearchClient.cat().aliases(aliasesRequest);
			return aliasesResponse.valueBody().stream()
				.map(AliasesRecord::alias)
				.filter(Objects::nonNull)
				.filter(alias -> !alias.startsWith(PREFIX_DOT) && !alias.startsWith(PREFIX_SECURITY))
				.filter(alias -> alias.startsWith(PREFIX_ALIAS))
				.toList();
		} catch (IOException e) {
			throw new CustomException(ErrorCode.OPENSEARCH_OPERATION_FAILED);
		}
	}

	@Override
	@Transactional
	@CacheEvict(value = "aliasesCache", allEntries = true)
	public DeleteIndexResponse deleteIndex(String target) {
		try {
			DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest.Builder().index(target).build();
			return openSearchClient.indices().delete(deleteIndexRequest);
		} catch (IOException e) {
			throw new CustomException(ErrorCode.OPENSEARCH_OPERATION_FAILED);
		}
	}
}
