package me.donggyeong.indexer.repository;

import me.donggyeong.indexer.config.QuerydslConfiguration;
import me.donggyeong.indexer.domain.RawData;
import me.donggyeong.indexer.enums.ActionEnum;

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
public class RawDataRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private RawDataRepository rawDataRepository;

	@Test
	public void save() {
		// given
		Map<String, Object> document = new HashMap<>();
		document.put("key1", "value1");
		document.put("key2", 123);
		RawData rawData = RawData.builder()
			.action(ActionEnum.from("create"))
			.document(document)
			.isValid(true)
			.build();

		// when
		RawData savedRawData = rawDataRepository.save(rawData);

		// then
		assertThat(savedRawData).isNotNull();
		assertThat(savedRawData.getId()).isNotNull();
		assertThat(savedRawData.getAction()).isEqualTo(ActionEnum.from("create"));
		assertThat(savedRawData.getDocument()).isEqualTo(document);
		assertThat(savedRawData.getIsValid()).isTrue();
		assertThat(savedRawData.getCreatedAt()).isNotNull();
	}

	@Test
	public void findByCreatedAtAfterAndIsValidTrue() {
		// given
		ZonedDateTime baseTime = ZonedDateTime.now().minusMinutes(1);
		RawData rawData1 = createRawData("index", true, baseTime.minusHours(10));
		RawData rawData2 = createRawData("create", true, baseTime.plusMinutes(1));
		RawData rawData3 = createRawData("update", false, baseTime.minusMinutes(40));
		RawData rawData4 = createRawData("delete", true, baseTime.plusMonths(1));

		entityManager.persist(rawData1);
		entityManager.persist(rawData2);
		entityManager.persist(rawData3);
		entityManager.persist(rawData4);
		entityManager.flush();

		// when
		List<RawData> foundRawData = rawDataRepository.findByCreatedAtAfterAndIsValidTrue(baseTime);

		// then
		assertThat(foundRawData).hasSize(2);
		assertThat(foundRawData).extracting(RawData::getAction).containsExactlyInAnyOrder(ActionEnum.from("create"), ActionEnum.from("delete"));
		assertThat(foundRawData).extracting(RawData::getIsValid).containsOnly(true);
		assertThat(foundRawData).extracting(RawData::getCreatedAt).allSatisfy(createdAt ->
			assertThat(createdAt).isAfter(baseTime)
		);
	}

	private RawData createRawData(String action, boolean isValid, ZonedDateTime createdAt) {
		Map<String, Object> document = new HashMap<>();
		document.put("key", "value-" + action);
		RawData rawData = RawData.builder()
			.action(ActionEnum.from(action))
			.document(document)
			.isValid(isValid)
			.build();
		rawData.update(createdAt);
		return rawData;
	}
}