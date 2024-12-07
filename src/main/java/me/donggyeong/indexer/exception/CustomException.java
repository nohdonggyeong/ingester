package me.donggyeong.indexer.exception;

import lombok.Getter;
import me.donggyeong.indexer.enums.ErrorCode;

@Getter
public class CustomException extends RuntimeException {
	private final ErrorCode errorCode;

	public CustomException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
