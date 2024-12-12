package me.donggyeong.indexer.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.donggyeong.indexer.dto.ConsumedItemRequest;
import me.donggyeong.indexer.dto.ConsumedItemResponse;
import me.donggyeong.indexer.enums.Action;
import me.donggyeong.indexer.service.ConsumedItemService;

@WebMvcTest(ConsumedItemController.class)
class ConsumedItemControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ConsumedItemService consumedItemService;

	@Autowired
	private ObjectMapper objectMapper;

	private static final String API_BASE_PATH = "/api/v1/consumed-item";
	private static final String JSON_ROOT_PATH = "$";
	private static final String JSON_FIRST_ELEMENT_PATH = "$[0]";

	@Test
	void save() throws Exception {
		// given
		when(consumedItemService.save(any(ConsumedItemRequest.class))).thenReturn(mock(ConsumedItemResponse.class));
		ConsumedItemRequest consumedItemRequest = new ConsumedItemRequest(
			Action.CREATE,
			"blog",
			"1",
			Map.of("category", "test-category", "title", "test-title", "description", "test-description")
		);

		// when
		mockMvc.perform(
			post(UriComponentsBuilder.fromPath(API_BASE_PATH).toUriString())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(consumedItemRequest))
		)
		// then
			.andExpect(status().isCreated())
			.andExpect(jsonPath(JSON_ROOT_PATH).isNotEmpty());
		verify(consumedItemService, times(1)).save(any(ConsumedItemRequest.class));
	}
}