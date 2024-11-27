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
		String tenant = "newTenant";
		IndexDetails indexDetails = IndexDetails.builder()
			.tenant(tenant)
			.build();

		// when
		IndexDetails savedIndexDetails = indexDetailsRepository.save(indexDetails);

		// then
		assertThat(savedIndexDetails).isNotNull();
		assertThat(savedIndexDetails.getId()).isNotNull(); // Assuming there's an ID generated
		assertThat(savedIndexDetails.getTenant()).isEqualTo(tenant);

		// Verify retrieval
		Optional<IndexDetails> retrievedIndexDetails = indexDetailsRepository.findByTenant(tenant);
		assertThat(retrievedIndexDetails).isPresent();
		assertThat(retrievedIndexDetails.get().getTenant()).isEqualTo(tenant);
	}

	@Test
	void findByTenant() {
		// given
		String tenant = "testTenant";
		IndexDetails indexDetails = IndexDetails.builder()
			.tenant(tenant)
			.build();
		indexDetailsRepository.save(indexDetails);

		// when
		Optional<IndexDetails> result = indexDetailsRepository.findByTenant(tenant);

		// then
		assertThat(result).isPresent();
		assertThat(result.get().getTenant()).isEqualTo(tenant);
	}
}
