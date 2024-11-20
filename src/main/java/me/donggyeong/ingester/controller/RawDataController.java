package me.donggyeong.ingester.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.donggyeong.ingester.dto.RawDataResponse;
import me.donggyeong.ingester.dto.RawDataRequest;
import me.donggyeong.ingester.service.RawDataService;
import me.donggyeong.ingester.service.RawDataServiceImpl;

@RestController
@RequestMapping("/api/v1/raw-data")
@RequiredArgsConstructor
public class RawDataController {
	private final RawDataService rawDataService;

	@PostMapping
	public ResponseEntity<RawDataResponse> createRawData(@RequestBody RawDataRequest rawDataRequest) {
		RawDataResponse rawDataResponse = rawDataService.createRawData(rawDataRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(rawDataResponse);
	}

	@GetMapping
	public ResponseEntity<List<RawDataResponse>> getRawDataAfterOffset() {
		List<RawDataResponse> rawDataResponseList = rawDataService.getRawDataAfterOffset();
		return ResponseEntity.status(HttpStatus.OK).body(rawDataResponseList);
	}
}
