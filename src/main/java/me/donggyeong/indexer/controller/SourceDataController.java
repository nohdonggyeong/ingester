package me.donggyeong.indexer.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.donggyeong.indexer.dto.SourceDataResponse;
import me.donggyeong.indexer.dto.SourceDataRequest;
import me.donggyeong.indexer.service.SourceDataService;

@RestController
@RequestMapping("/api/v1/source-data")
@RequiredArgsConstructor
public class SourceDataController {
	private final SourceDataService sourceDataService;

	@PostMapping
	public ResponseEntity<SourceDataResponse> createSourceData(@RequestBody SourceDataRequest sourceDataRequest) {
		SourceDataResponse sourceDataResponse = sourceDataService.createSourceData(sourceDataRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(sourceDataResponse);
	}

	@GetMapping
	public ResponseEntity<List<SourceDataResponse>> getSourceDataAfterOffset() {
		List<SourceDataResponse> sourceDataResponseList = sourceDataService.getSourceDataAfterOffset();
		return ResponseEntity.status(HttpStatus.OK).body(sourceDataResponseList);
	}
}
