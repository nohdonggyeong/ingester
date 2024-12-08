package me.donggyeong.indexer.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import me.donggyeong.indexer.dto.SourceDataRequest;
import me.donggyeong.indexer.dto.SourceDataResponse;
import me.donggyeong.indexer.enums.Action;
import me.donggyeong.indexer.repository.SourceDataRepository;

@SpringBootTest
class SourceDataServiceTest {
	@Autowired
	private SourceDataService sourceDataService;
	@Autowired
	private SourceDataRepository sourceDataRepository;

	@AfterEach
	void tearDown() {
		sourceDataRepository.deleteAll();
	}

	@Test
	void createSourceData() {
		// given
		Map<String, Object> data = new HashMap<>();
		data.put("category", "test-category");
		data.put("title", "test-title");
		data.put("description", "test-description");
		SourceDataRequest sourceDataRequest = new SourceDataRequest(Action.CREATE, "hub", 1L, data);

		// when
		SourceDataResponse sourceDataResponse = sourceDataService.createSourceData(sourceDataRequest);

		// then
		assertNotNull(sourceDataResponse);
		assertMapEquals(data, sourceDataResponse.getData());
		assertNotNull(sourceDataResponse.getConsumedAt());
	}

	@Test
	void getSourceDataAfterOffset() {
		// given
		ZonedDateTime offsetTime = ZonedDateTime.now(ZoneId.of("UTC"));
		Map<String, Object> data = new HashMap<>();
		data.put("category", "test-category");
		data.put("title", "test-title");
		data.put("description", "test-description");
		SourceDataRequest sourceDataRequest = new SourceDataRequest(Action.CREATE, "hub", 1L, data);
		sourceDataService.createSourceData(sourceDataRequest);

		// when
		List<SourceDataResponse> sourceDataResponseList = sourceDataService.getSourceDataAfterOffset(offsetTime);

		// then
		assertNotNull(sourceDataResponseList);
		assertFalse(sourceDataResponseList.isEmpty());
		assertEquals(1, sourceDataResponseList.size());

		SourceDataResponse sourceDataResponse = sourceDataResponseList.getFirst();
		assertEquals(Action.CREATE, sourceDataResponse.getAction());
		assertEquals("hub", sourceDataResponse.getSource());
		assertEquals(1L, sourceDataResponse.getDataId());
		assertMapEquals(data, sourceDataResponse.getData());
	}

	// Helper method to compare maps ignoring their specific implementations
	private void assertMapEquals(Map<String, Object> expected, Map<String, Object> actual) {
		assertEquals(expected.size(), actual.size());
		for (Map.Entry<String, Object> entry : expected.entrySet()) {
			assertTrue(actual.containsKey(entry.getKey()));
			Object expectedValue = entry.getValue();
			Object actualValue = actual.get(entry.getKey());
			if (expectedValue instanceof Number && actualValue instanceof Number) {
				assertEquals(((Number) expectedValue).longValue(), ((Number) actualValue).longValue());
			} else {
				assertEquals(expectedValue, actualValue);
			}
		}
	}
}