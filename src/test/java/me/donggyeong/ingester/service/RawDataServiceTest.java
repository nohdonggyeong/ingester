package me.donggyeong.ingester.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import me.donggyeong.ingester.domain.RawData;
import me.donggyeong.ingester.dto.RawDataRequest;
import me.donggyeong.ingester.dto.RawDataResponse;
import me.donggyeong.ingester.repository.RawDataRepository;

@ExtendWith(MockitoExtension.class)
class RawDataServiceTest {
	@Mock
	private RawDataRepository rawDataRepository;

	@InjectMocks
	private RawDataServiceImpl rawDataServiceImpl;

	@Test
	void createRawData() {
		when(rawDataRepository.save(any(RawData.class))).thenReturn(mock(RawData.class));

		RawDataResponse rawDataResponse = rawDataServiceImpl.createRawData(new RawDataRequest());

		assertNotNull(rawDataResponse);
		verify(rawDataRepository, times(1)).save(any(RawData.class));
	}

	@Test
	void getRawDataAfterOffset() {
		when(rawDataRepository.findByCreatedAtAfterAndIsValidTrue(any(ZonedDateTime.class)))
			.thenReturn(Collections.singletonList(mock(RawData.class)));

		List<RawDataResponse> rawDataResponseList = rawDataServiceImpl.getRawDataAfterOffset();

		assertNotNull(rawDataResponseList);
		assertFalse(rawDataResponseList.isEmpty());
		assertEquals(1, rawDataResponseList.size());
		verify(rawDataRepository, times(1)).findByCreatedAtAfterAndIsValidTrue(any(ZonedDateTime.class));
	}
}