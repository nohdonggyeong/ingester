package me.donggyeong.indexer.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {
	ITEM_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "The item could not be found."),
	ACTION_NOT_SUPPORTED(HttpStatus.BAD_REQUEST.value(), "This action is not supported."),
	OPENSEARCH_CREATE_INDEX_OPERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR.value(), "A failure occurred during the process of creating the index."),
	OPENSEARCH_BULK_INDEXING_OPERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR.value(), "A failure occurred during bulk indexing."),
	OPENSEARCH_CHECK_INDEX_EXISTS_OPERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR.value(), "A failure occurred while checking whether the index exists."),
	OPENSEARCH_FIND_ALL_ALIASES_OPERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR.value(), "A failure occurred while retrieving all aliases."),
	OPENSEARCH_DELETE_INDEX_OPERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR.value(), "A failure occurred while deleting the index.");

	private final int statusCode;
	private final String message;

	ErrorCode(int statusCode, String message) {
		this.statusCode = statusCode;
		this.message = message;
	}
}
