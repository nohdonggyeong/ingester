package me.donggyeong.indexer.dto;

public class ResponseError {
	private int statusCode;
	private String errorCode;
	private String message;

	public ResponseError(int statusCode, String errorCode, String message) {
		this.statusCode = statusCode;
		this.errorCode = errorCode;
		this.message = message;
	}

	// Getters and Setters
}
