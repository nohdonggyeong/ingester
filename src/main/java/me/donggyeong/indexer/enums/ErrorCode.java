package me.donggyeong.indexer.enums;

public enum ErrorCode {
	INVALID_REQUEST(400, "Invalid request data"),
	SOURCE_DATA_NOT_FOUND(404, "Source data not found"),
	DATA_ALREADY_EXISTS(409, "Data already exists"),
	SERVER_ERROR(500, "Internal server error");

	private final int statusCode;
	private final String message;

	ErrorCode(int statusCode, String message) {
		this.statusCode = statusCode;
		this.message = message;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getMessage() {
		return message;
	}
}
