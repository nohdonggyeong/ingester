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

import me.donggyeong.indexer.dto.ItemRequest;
import me.donggyeong.indexer.dto.ItemResponse;
import me.donggyeong.indexer.enums.Action;
import me.donggyeong.indexer.service.ItemService;

@WebMvcTest(ItemController.class)
class ItemControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ItemService itemService;

	@Autowired
	private ObjectMapper objectMapper;

	private static final String API_BASE_PATH = "/api/v1/item";
	private static final String JSON_ROOT_PATH = "$";
	private static final String JSON_FIRST_ELEMENT_PATH = "$[0]";

	@Test
	void save() throws Exception {
		// given
		when(itemService.save(any(ItemRequest.class))).thenReturn(mock(ItemResponse.class));
		ItemRequest itemRequest = new ItemRequest(
			Action.CREATE,
			"blog",
			"1",
			Map.of("category", "test-category", "title", "test-title", "description", "test-description")
		);

		// when
		mockMvc.perform(
			post(UriComponentsBuilder.fromPath(API_BASE_PATH).toUriString())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(itemRequest))
		)
		// then
			.andExpect(status().isCreated())
			.andExpect(jsonPath(JSON_ROOT_PATH).isNotEmpty());
		verify(itemService, times(1)).save(any(ItemRequest.class));
	}
}