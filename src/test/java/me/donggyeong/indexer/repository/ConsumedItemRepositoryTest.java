package me.donggyeong.indexer.repository;

import me.donggyeong.indexer.config.QuerydslConfig;
import me.donggyeong.indexer.entity.ConsumedItem;
import me.donggyeong.indexer.enums.Action;
import me.donggyeong.indexer.enums.IndexingState;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
public class ConsumedItemRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private ConsumedItemRepository consumedItemRepository;

	@Test
	public void save() {
		// given
		Map<String, Object> documentBody = new HashMap<>();
		documentBody.put("category", "test-category");
		documentBody.put("title", "test-title");
		documentBody.put("description", "test-description");
		ConsumedItem consumedItem = ConsumedItem.builder()
			.action(Action.CREATE)
			.target("blog")
			.docId("1")
			.docBody(documentBody)
			.build();

		// when
		ConsumedItem savedConsumedItem = consumedItemRepository.save(consumedItem);

		// then
		assertThat(savedConsumedItem).isNotNull();
		assertThat(savedConsumedItem.getId()).isNotNull();
		assertThat(savedConsumedItem.getDocBody()).isEqualTo(documentBody);
		assertThat(savedConsumedItem.getConsumedAt()).isNotNull();
	}

	@Test
	public void findByIndexingStateOrderByConsumedAt() {
		// given
		Map<String, Object> documentBody = new HashMap<>();
		documentBody.put("category", "test-category");
		documentBody.put("title", "test-title");
		documentBody.put("description", "test-description");

		ConsumedItem consumedItem1 = ConsumedItem.builder()
			.action(Action.CREATE)
			.target("blog")
			.docId("1")
			.docBody(documentBody)
			.build();

		ConsumedItem consumedItem2 = ConsumedItem.builder()
			.action(Action.UPDATE)
			.target("blog")
			.docId("2")
			.docBody(documentBody)
			.build();

		entityManager.persist(consumedItem1);

		entityManager.persist(consumedItem2);

		entityManager.flush();

		// when
		List<ConsumedItem> foundSourceData = consumedItemRepository.findByIndexingStateOrderByConsumedAt(IndexingState.PENDING);

		// then
		assertThat(foundSourceData).hasSize(2);
		assertThat(foundSourceData).extracting(ConsumedItem::getIndexingState).allSatisfy(indexingState ->
			assertThat(indexingState).isEqualTo(IndexingState.PENDING)
		);
	}
}