package me.donggyeong.indexer.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import me.donggyeong.indexer.dto.ResponseError;
import me.donggyeong.indexer.exception.CustomException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ResponseError> handleCustomException(CustomException e) {
		ResponseError responseError = new ResponseError(e.getErrorCode().getStatusCode(), e.getErrorCode().name(), e.getMessage());
		return new ResponseEntity<>(responseError, HttpStatus.valueOf(e.getErrorCode().getStatusCode()));
	}
}
