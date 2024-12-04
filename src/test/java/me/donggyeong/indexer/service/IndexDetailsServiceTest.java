package me.donggyeong.indexer.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import me.donggyeong.indexer.dto.IndexDetailsResponse;
import me.donggyeong.indexer.entity.IndexDetails;
import me.donggyeong.indexer.repository.IndexDetailsRepository;

@SpringBootTest
class IndexDetailsServiceTest {

	@Autowired
	private IndexDetailsServiceImpl indexDetailsService;

	@Autowired
	private IndexDetailsRepository indexDetailsRepository;

	@AfterEach
	void tearDown() {
		indexDetailsRepository.deleteAll();
	}

	@Test
	void createIndexDetails() {
		// given
		String source = "testSource";

		// when
		IndexDetailsResponse response = indexDetailsService.createIndexDetails(source);

		// then
		assertThat(response).isNotNull();
		assertThat(response.getSource()).isEqualTo(source);

		// Verify that it was saved in the repository
		Optional<IndexDetails> savedIndexDetails = indexDetailsRepository.findBySource(source);
		assertThat(savedIndexDetails).isPresent();
		assertThat(savedIndexDetails.get().getSource()).isEqualTo(source);
	}

	@Test
	void getIndexDetailsBySource() {
		// given
		String source = "testSource";
		indexDetailsRepository.save(IndexDetails.builder().source(source).build());

		// when
		IndexDetailsResponse response = indexDetailsService.getIndexDetailsBySource(source);

		// then
		assertThat(response).isNotNull();
		assertThat(response.getSource()).isEqualTo(source);
	}

	@Test
	void updateLastIndexedAt() {
		// given
		String source = "testSource";
		IndexDetails indexDetails = IndexDetails.builder().source(source).build();

		// Save the IndexDetails to the repository.
		indexDetailsRepository.save(indexDetails);

		// when
		IndexDetailsResponse response = indexDetailsService.updateLastIndexedAt(source);

		// then
		assertThat(response).isNotNull();
		assertThat(response.getSource()).isEqualTo(source);
		assertThat(response.getLastIndexedAt()).isNotNull();  // Assuming this method exists and returns a timestamp.
	}
}
