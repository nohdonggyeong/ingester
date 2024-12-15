package me.donggyeong.indexer.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import me.donggyeong.indexer.utils.TestUtils;

@WebMvcTest(ItemController.class)
class ItemControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ItemService itemService;

	@Autowired
	private ObjectMapper objectMapper;

	private static final String API_BASE_PATH = "/api/v1/item";

	@Test
	void testSave() throws Exception {
		// given
		when(itemService.save(any(ItemRequest.class))).thenReturn(mock(ItemResponse.class));

		ItemRequest itemRequest = new ItemRequest();
		itemRequest.setAction(Action.CREATE);
		itemRequest.setTarget(TestUtils.TARGET);
		itemRequest.setDocId(TestUtils.DOC_ID_1);
		itemRequest.setDocBody(TestUtils.DOC_BODY);

		// when
		mockMvc.perform(
			post(UriComponentsBuilder.fromPath(API_BASE_PATH).toUriString())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(itemRequest))
		)
		// then
			.andExpect(status().isCreated())
			.andExpect(jsonPath(TestUtils.JSON_ROOT_PATH).isNotEmpty());

		verify(itemService, times(1)).save(any(ItemRequest.class));
	}
}