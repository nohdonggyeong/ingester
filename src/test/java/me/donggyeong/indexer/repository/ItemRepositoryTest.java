package me.donggyeong.indexer.repository;

import me.donggyeong.indexer.config.QuerydslConfig;
import me.donggyeong.indexer.entity.Item;
import me.donggyeong.indexer.enums.Action;
import me.donggyeong.indexer.utils.TestUtils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
public class ItemRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private ItemRepository itemRepository;

	@Test
	public void testSave() {
		// Given
		Item item = Item.builder()
			.action(Action.CREATE)
			.target(TestUtils.TARGET)
			.docId(TestUtils.DOC_ID_1)
			.docBody(TestUtils.DOC_BODY)
			.build();

		// When
		Item savedItem = itemRepository.save(item);

		// Then
		assertThat(savedItem).isNotNull();
		assertThat(savedItem.getId()).isNotNull();
		assertThat(savedItem.getDocBody()).isEqualTo(TestUtils.DOC_BODY);
		assertThat(savedItem.getConsumedAt()).isNotNull();

		// Additional assertions
		assertThat(savedItem.getAction()).isEqualTo(Action.CREATE);
		assertThat(savedItem.getTarget()).isEqualTo(TestUtils.TARGET);
		assertThat(savedItem.getDocId()).isEqualTo(TestUtils.DOC_ID_1);
	}

	@Test
	public void testFindByStatusIsNullOrderByConsumedAtAsc() {
		// Given
		Item item1 = createItem(TestUtils.DOC_ID_1, new HashMap<>());
		Item item2 = createItem(TestUtils.DOC_ID_2, new HashMap<>());
		Item item3 = createItem(TestUtils.DOC_ID_3, new HashMap<>());

		item3.update(TestUtils.INDEX, HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase(), null, ZonedDateTime.now(ZoneId.of("UTC")));

		entityManager.persist(item1);
		entityManager.persist(item2);
		entityManager.persist(item3);
		entityManager.flush();

		// When
		List<Item> result = itemRepository.findByStatusIsNullOrderByConsumedAtAsc();

		// Then
		assertThat(result).hasSize(2);
		assertThat(result).extracting(Item::getDocId).containsExactly(TestUtils.DOC_ID_1, TestUtils.DOC_ID_2);
		assertThat(result).extracting(Item::getStatus).containsOnlyNulls();
		assertThat(result.get(0).getConsumedAt()).isBeforeOrEqualTo(result.get(1).getConsumedAt());
	}

	@Test
	public void testFindById() {
		// Given
		Item item = Item.builder()
			.action(Action.CREATE)
			.target(TestUtils.TARGET)
			.docId(TestUtils.DOC_ID_1)
			.docBody(TestUtils.DOC_BODY)
			.build();

		Item savedItem = entityManager.persist(item);
		entityManager.flush();

		// When
		Optional<Item> foundItem = itemRepository.findById(savedItem.getId());

		// Then
		assertThat(foundItem).isPresent();
		assertThat(foundItem.get().getId()).isEqualTo(savedItem.getId());
		assertThat(foundItem.get().getAction()).isEqualTo(Action.CREATE);
		assertThat(foundItem.get().getTarget()).isEqualTo(TestUtils.TARGET);
		assertThat(foundItem.get().getDocId()).isEqualTo(TestUtils.DOC_ID_1);
		assertThat(foundItem.get().getDocBody()).isEqualTo(TestUtils.DOC_BODY);
	}

	private Item createItem(String docId, Map<String, Object> docBody) {
		return Item.builder()
			.action(Action.CREATE)
			.target(TestUtils.TARGET)
			.docId(docId)
			.docBody(docBody)
			.build();
	}
}
