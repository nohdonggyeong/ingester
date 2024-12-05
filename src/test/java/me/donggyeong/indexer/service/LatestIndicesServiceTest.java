package me.donggyeong.indexer.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import me.donggyeong.indexer.dto.LatestIndicesResponse;
import me.donggyeong.indexer.entity.LatestIndices;
import me.donggyeong.indexer.repository.LatestIndicesRepository;

@SpringBootTest
class LatestIndicesServiceTest {

	@Autowired
	private LatestIndicesServiceImpl latestIndicesService;

	@Autowired
	private LatestIndicesRepository latestIndicesRepository;

	@AfterEach
	void tearDown() {
		latestIndicesRepository.deleteAll();
	}

	@Test
	void createLatestIndex() {
		// given
		String source = "testSource";

		// when
		LatestIndicesResponse response = latestIndicesService.createLatestIndex(source);

		// then
		assertThat(response).isNotNull();
		assertThat(response.getSource()).isEqualTo(source);

		// Verify that it was saved in the repository
		Optional<LatestIndices> latestIndicesOptional = latestIndicesRepository.findBySource(source);
		assertThat(latestIndicesOptional).isPresent();
		assertThat(latestIndicesOptional.get().getSource()).isEqualTo(source);
	}

	@Test
	void getLatestIndexBySource() {
		// given
		String source = "testSource";
		latestIndicesRepository.save(LatestIndices.builder().source(source).build());

		// when
		LatestIndicesResponse response = latestIndicesService.getLatestIndexBySource(source);

		// then
		assertThat(response).isNotNull();
		assertThat(response.getSource()).isEqualTo(source);
	}

	@Test
	void updateLastIndexedAt() {
		// given
		String source = "testSource";
		LatestIndices latestIndices = LatestIndices.builder().source(source).build();

		// Save the LatestIndices to the repository.
		latestIndicesRepository.save(latestIndices);

		// when
		LatestIndicesResponse response = latestIndicesService.updateLastIndexedAt(source);

		// then
		assertThat(response).isNotNull();
		assertThat(response.getSource()).isEqualTo(source);
		assertThat(response.getLastIndexedAt()).isNotNull();  // Assuming this method exists and returns a timestamp.
	}
}
