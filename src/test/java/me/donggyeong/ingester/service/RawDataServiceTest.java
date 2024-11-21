package me.donggyeong.ingester.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import me.donggyeong.ingester.domain.RawData;
import me.donggyeong.ingester.dto.RawDataRequest;
import me.donggyeong.ingester.dto.RawDataResponse;
import me.donggyeong.ingester.repository.RawDataRepository;

@SpringBootTest
class RawDataServiceTest {
	@Autowired
	private RawDataService rawDataService;
	@Autowired
	private RawDataRepository rawDataRepository;

	@AfterEach
	void tearDown() {
		rawDataRepository.deleteAll();
	}

	@Test
	void createRawData() {
		// given
		Map<String, Object> document = new HashMap<>();
		document.put("tenant", "test");
		document.put("id", 1L);
		RawDataRequest rawDataRequest = new RawDataRequest("CREATE", document);

		// when
		RawDataResponse rawDataResponse = rawDataService.createRawData(rawDataRequest);

		// then
		assertNotNull(rawDataResponse);
		assertEquals("CREATE", rawDataResponse.getAction());
		assertMapEquals(document, rawDataResponse.getDocument());
		assertTrue(rawDataResponse.getIsValid());
		assertNotNull(rawDataResponse.getCreatedAt());

		RawData rawData = rawDataRepository.findFirstByOrderByIdDesc();
		assertNotNull(rawData);
		assertEquals("CREATE", rawData.getAction());
		assertMapEquals(document, rawData.getDocument());
		assertTrue(rawData.getIsValid());
		assertNotNull(rawData.getCreatedAt());
	}

	@Test
	void getRawDataAfterOffset() {
		// given
		Map<String, Object> document = new LinkedHashMap<>();
		document.put("tenant", "test");
		document.put("id", 1L);
		RawDataRequest rawDataRequest = new RawDataRequest("CREATE", document);
		rawDataService.createRawData(rawDataRequest);

		// when
		List<RawDataResponse> rawDataResponseList = rawDataService.getRawDataAfterOffset();

		// then
		assertNotNull(rawDataResponseList);
		assertFalse(rawDataResponseList.isEmpty());
		assertEquals(1, rawDataResponseList.size());

		RawDataResponse rawDataResponse = rawDataResponseList.getFirst();
		assertEquals("CREATE", rawDataResponse.getAction());
		assertEquals("test", rawDataResponse.getDocument().get("tenant"));
		assertEquals(1L, ((Number) rawDataResponse.getDocument().get("id")).longValue());
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