package me.donggyeong.indexer.repository;

import me.donggyeong.indexer.config.QuerydslConfiguration;
import me.donggyeong.indexer.domain.SourceData;
import me.donggyeong.indexer.enumType.Action;

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
@Import(QuerydslConfiguration.class)
public class SourceDataRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private SourceDataRepository sourceDataRepository;

	@Test
	public void save() {
		// given
		Map<String, Object> document = new HashMap<>();
		document.put("key1", "value1");
		document.put("key2", 123);
		SourceData sourceData = SourceData.builder()
			.action(Action.from("create"))
			.document(document)
			.isValid(true)
			.build();

		// when
		SourceData savedSourceData = sourceDataRepository.save(sourceData);

		// then
		assertThat(savedSourceData).isNotNull();
		assertThat(savedSourceData.getId()).isNotNull();
		assertThat(savedSourceData.getAction()).isEqualTo(Action.from("create"));
		assertThat(savedSourceData.getDocument()).isEqualTo(document);
		assertThat(savedSourceData.getIsValid()).isTrue();
		assertThat(savedSourceData.getCreatedAt()).isNotNull();
	}

	@Test
	public void findByCreatedAtAfterAndIsValidTrue() {
		// given
		ZonedDateTime baseTime = ZonedDateTime.now().minusMinutes(1);
		SourceData sourceData1 = createSourceData("index", true, baseTime.minusHours(10));
		SourceData sourceData2 = createSourceData("create", true, baseTime.plusMinutes(1));
		SourceData sourceData3 = createSourceData("update", false, baseTime.minusMinutes(40));
		SourceData sourceData4 = createSourceData("delete", true, baseTime.plusMonths(1));

		entityManager.persist(sourceData1);
		entityManager.persist(sourceData2);
		entityManager.persist(sourceData3);
		entityManager.persist(sourceData4);
		entityManager.flush();

		// when
		List<SourceData> foundSourceData = sourceDataRepository.findByCreatedAtAfterAndIsValidTrue(baseTime);

		// then
		assertThat(foundSourceData).hasSize(2);
		assertThat(foundSourceData).extracting(SourceData::getAction).containsExactlyInAnyOrder(Action.from("create"), Action.from("delete"));
		assertThat(foundSourceData).extracting(SourceData::getIsValid).containsOnly(true);
		assertThat(foundSourceData).extracting(SourceData::getCreatedAt).allSatisfy(createdAt ->
			assertThat(createdAt).isAfter(baseTime)
		);
	}

	private SourceData createSourceData(String action, boolean isValid, ZonedDateTime createdAt) {
		Map<String, Object> document = new HashMap<>();
		document.put("key", "value-" + action);
		SourceData sourceData = SourceData.builder()
			.action(Action.from(action))
			.document(document)
			.isValid(isValid)
			.build();
		sourceData.update(createdAt);
		return sourceData;
	}
}