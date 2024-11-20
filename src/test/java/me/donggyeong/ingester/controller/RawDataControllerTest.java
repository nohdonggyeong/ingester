package me.donggyeong.ingester.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.donggyeong.ingester.dto.RawDataRequest;
import me.donggyeong.ingester.dto.RawDataResponse;
import me.donggyeong.ingester.service.RawDataService;

@WebMvcTest(RawDataController.class)
class RawDataControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RawDataService rawDataService;

	@Autowired
	private ObjectMapper objectMapper;

	private static final String API_PREFIX = "/api/v1/raw-data";
	private static final String JSON_ROOT_PATH = "$";
	private static final String JSON_FIRST_ELEMENT_PATH = "$[0]";

	@Test
	void createRawData() throws Exception {
		when(rawDataService.createRawData(any(RawDataRequest.class))).thenReturn(mock(RawDataResponse.class));

		mockMvc.perform(
			post(UriComponentsBuilder.fromPath(API_PREFIX).toUriString())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new RawDataRequest()))
		)
			.andExpect(status().isCreated())
			.andExpect(jsonPath(JSON_ROOT_PATH).isNotEmpty());

		verify(rawDataService, times(1)).createRawData(any(RawDataRequest.class));
	}

	@Test
	void getRawDataAfterOffset() throws Exception {
		when(rawDataService.getRawDataAfterOffset()).thenReturn(Arrays.asList(mock(RawDataResponse.class)));

		mockMvc.perform(
			get(UriComponentsBuilder.fromPath(API_PREFIX).toUriString())
				.contentType(MediaType.APPLICATION_JSON)
		)
			.andExpect(status().isOk())
			.andExpect(jsonPath(JSON_ROOT_PATH).isArray())
			.andExpect(jsonPath(JSON_FIRST_ELEMENT_PATH).isNotEmpty());

		verify(rawDataService, times(1)).getRawDataAfterOffset();
	}
}