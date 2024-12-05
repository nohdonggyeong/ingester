package me.donggyeong.indexer.repository;

import me.donggyeong.indexer.config.QuerydslConfig;
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
		data.put("action", Action.CREATE.toString());
		data.put("source", "hub");
		data.put("id", "123");
		SourceData sourceData = SourceData.builder()
			.data(data)
			.build();

		// when
		SourceData savedSourceData = sourceDataRepository.save(sourceData);

		// then
		assertThat(savedSourceData).isNotNull();
		assertThat(savedSourceData.getId()).isNotNull();
		assertThat(savedSourceData.getData()).isEqualTo(data);
		assertThat(savedSourceData.getIsValid()).isTrue();
		assertThat(savedSourceData.getConsumedAt()).isNotNull();
	}

	@Test
	public void findByConsumedAtAfterAndIsValidTrue() {
		// given
		Map<String, Object> data1 = new HashMap<>();
		data1.put("action", Action.CREATE.toString());
		data1.put("source", "hub");
		data1.put("id", "123");
		Map<String, Object> data2 = new HashMap<>();
		data2.put("action", Action.UPDATE.toString());
		data2.put("id", "123");
		SourceData sourceData1 = SourceData.builder()
			.data(data1)
			.build();
		SourceData sourceData2 = SourceData.builder()
			.data(data2)
			.build();
		entityManager.persist(sourceData1);
		entityManager.persist(sourceData2);
		entityManager.flush();

		// when
		ZonedDateTime baseTime = ZonedDateTime.now().minusMinutes(1);
		List<SourceData> foundSourceData = sourceDataRepository.findByConsumedAtAfterAndIsValidTrue(baseTime);

		// then
		assertThat(foundSourceData).hasSize(1);
		assertThat(foundSourceData).extracting(SourceData::getIsValid).containsOnly(true);
		assertThat(foundSourceData).extracting(SourceData::getConsumedAt).allSatisfy(consumedAt ->
			assertThat(consumedAt).isAfter(baseTime)
		);
	}
}