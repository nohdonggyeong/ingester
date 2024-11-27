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
		String tenant = "testTenant";

		// when
		IndexDetailsResponse response = indexDetailsService.createIndexDetails(tenant);

		// then
		assertThat(response).isNotNull();
		assertThat(response.getTenant()).isEqualTo(tenant);

		// Verify that it was saved in the repository
		Optional<IndexDetails> savedIndexDetails = indexDetailsRepository.findByTenant(tenant);
		assertThat(savedIndexDetails).isPresent();
		assertThat(savedIndexDetails.get().getTenant()).isEqualTo(tenant);
	}

	@Test
	void getIndexDetailsByTenant() {
		// given
		String tenant = "testTenant";
		indexDetailsRepository.save(IndexDetails.builder().tenant(tenant).build());

		// when
		IndexDetailsResponse response = indexDetailsService.getIndexDetailsByTenant(tenant);

		// then
		assertThat(response).isNotNull();
		assertThat(response.getTenant()).isEqualTo(tenant);
	}

	@Test
	void updateLastIndexedAt() {
		// given
		String tenant = "testTenant";
		IndexDetails indexDetails = IndexDetails.builder().tenant(tenant).build();

		// Save the IndexDetails to the repository.
		indexDetailsRepository.save(indexDetails);

		// when
		IndexDetailsResponse response = indexDetailsService.updateLastIndexedAt(tenant);

		// then
		assertThat(response).isNotNull();
		assertThat(response.getTenant()).isEqualTo(tenant);
		assertThat(response.getLastIndexedAt()).isNotNull();  // Assuming this method exists and returns a timestamp.
	}
}
