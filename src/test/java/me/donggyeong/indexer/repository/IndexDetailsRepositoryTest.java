package me.donggyeong.indexer.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import me.donggyeong.indexer.config.QuerydslConfig;
import me.donggyeong.indexer.entity.IndexDetails;

@DataJpaTest
@Import(QuerydslConfig.class)
class IndexDetailsRepositoryTest {

	@Autowired
	private IndexDetailsRepository indexDetailsRepository;

	@Test
	void save() {
		// given
		String source = "newSource";
		IndexDetails indexDetails = IndexDetails.builder()
			.source(source)
			.build();

		// when
		IndexDetails savedIndexDetails = indexDetailsRepository.save(indexDetails);

		// then
		assertThat(savedIndexDetails).isNotNull();
		assertThat(savedIndexDetails.getId()).isNotNull(); // Assuming there's an ID generated
		assertThat(savedIndexDetails.getSource()).isEqualTo(source);

		// Verify retrieval
		Optional<IndexDetails> retrievedIndexDetails = indexDetailsRepository.findBySource(source);
		assertThat(retrievedIndexDetails).isPresent();
		assertThat(retrievedIndexDetails.get().getSource()).isEqualTo(source);
	}

	@Test
	void findBySource() {
		// given
		String source = "testSource";
		IndexDetails indexDetails = IndexDetails.builder()
			.source(source)
			.build();
		indexDetailsRepository.save(indexDetails);

		// when
		Optional<IndexDetails> result = indexDetailsRepository.findBySource(source);

		// then
		assertThat(result).isPresent();
		assertThat(result.get().getSource()).isEqualTo(source);
	}
}
