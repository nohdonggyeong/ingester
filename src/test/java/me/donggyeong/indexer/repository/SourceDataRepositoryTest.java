package me.donggyeong.indexer.repository;

import me.donggyeong.indexer.config.QuerydslConfig;
import me.donggyeong.indexer.dto.SourceDataRequest;
import me.donggyeong.indexer.entity.SourceData;
import me.donggyeong.indexer.enums.Action;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
public class SourceDataRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private SourceDataRepository sourceDataRepository;

	@Test
	public void save() {
		// given
		Map<String, Object> data = new HashMap<>();
		data.put("category", "test-category");
		data.put("title", "test-title");
		data.put("description", "test-description");
		SourceData sourceData = SourceData.builder()
			.action(Action.CREATE)
			.source("hub")
			.dataId(1L)
			.data(data)
			.build();

		// when
		SourceData savedSourceData = sourceDataRepository.save(sourceData);

		// then
		assertThat(savedSourceData).isNotNull();
		assertThat(savedSourceData.getId()).isNotNull();
		assertThat(savedSourceData.getData()).isEqualTo(data);
		assertThat(savedSourceData.getConsumedAt()).isNotNull();
	}

	@Test
	public void findByConsumedAtAfterAndIsValidTrue() {
		// given
		Map<String, Object> data = new HashMap<>();
		data.put("category", "test-category");
		data.put("title", "test-title");
		data.put("description", "test-description");

		SourceData sourceData1 = SourceData.builder()
			.action(Action.CREATE)
			.source("hub")
			.dataId(1L)
			.data(data)
			.build();

		SourceData sourceData2 = SourceData.builder()
			.action(Action.UPDATE)
			.source("hub")
			.dataId(2L)
			.data(data)
			.build();

		entityManager.persist(sourceData1);

		ZonedDateTime baseTime = ZonedDateTime.now();

		entityManager.persist(sourceData2);

		entityManager.flush();

		// when
		List<SourceData> foundSourceData = sourceDataRepository.findByConsumedAtAfter(baseTime);

		// then
		assertThat(foundSourceData).hasSize(1);
		assertThat(foundSourceData).extracting(SourceData::getConsumedAt).allSatisfy(consumedAt ->
			assertThat(consumedAt).isAfter(baseTime)
		);
	}
}