package me.donggyeong.indexer.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import me.donggyeong.indexer.config.QuerydslConfig;
import me.donggyeong.indexer.entity.LatestIndices;

@DataJpaTest
@Import(QuerydslConfig.class)
class LatestIndicesRepositoryTest {

	@Autowired
	private LatestIndicesRepository latestIndicesRepository;

	@Test
	void save() {
		// given
		String source = "newSource";
		LatestIndices latestIndices = LatestIndices.builder()
			.source(source)
			.build();

		// when
		LatestIndices savedLatestIndices = latestIndicesRepository.save(latestIndices);

		// then
		assertThat(savedLatestIndices).isNotNull();
		assertThat(savedLatestIndices.getId()).isNotNull(); // Assuming there's an ID generated
		assertThat(savedLatestIndices.getSource()).isEqualTo(source);

		// Verify retrieval
		Optional<LatestIndices> latestIndicesOptional = latestIndicesRepository.findBySource(source);
		assertThat(latestIndicesOptional).isPresent();
		assertThat(latestIndicesOptional.get().getSource()).isEqualTo(source);
	}

	@Test
	void findBySource() {
		// given
		String source = "testSource";
		LatestIndices latestIndices = LatestIndices.builder()
			.source(source)
			.build();
		latestIndicesRepository.save(latestIndices);

		// when
		Optional<LatestIndices> result = latestIndicesRepository.findBySource(source);

		// then
		assertThat(result).isPresent();
		assertThat(result.get().getSource()).isEqualTo(source);
	}
}
