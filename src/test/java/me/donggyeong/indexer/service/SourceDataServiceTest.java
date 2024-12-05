package me.donggyeong.indexer.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import me.donggyeong.indexer.entity.SourceData;
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
		data.put("action", Action.CREATE.toString());
		data.put("source", "hub");
		data.put("id", "123");
		SourceDataRequest sourceDataRequest = new SourceDataRequest(data);

		// when
		SourceDataResponse sourceDataResponse = sourceDataService.createSourceData(sourceDataRequest);

		// then
		assertNotNull(sourceDataResponse);
		assertMapEquals(data, sourceDataResponse.getData());
		assertTrue(sourceDataResponse.getIsValid());
		assertNotNull(sourceDataResponse.getConsumedAt());
	}

	@Test
	void getSourceDataAfterOffset() {
		// given
		Map<String, Object> data = new HashMap<>();
		data.put("action", Action.CREATE.toString());
		data.put("source", "hub");
		data.put("id", "123");
		SourceDataRequest sourceDataRequest = new SourceDataRequest(data);
		sourceDataService.createSourceData(sourceDataRequest);

		// when
		List<SourceDataResponse> sourceDataResponseList = sourceDataService.getSourceDataAfterOffset();

		// then
		assertNotNull(sourceDataResponseList);
		assertFalse(sourceDataResponseList.isEmpty());
		assertEquals(1, sourceDataResponseList.size());

		SourceDataResponse sourceDataResponse = sourceDataResponseList.getFirst();
		assertEquals(Action.CREATE.toString(), sourceDataResponse.getData().get("action"));
		assertEquals("hub", sourceDataResponse.getData().get("source"));
		assertEquals("123", sourceDataResponse.getData().get("id"));
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