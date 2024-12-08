package me.donggyeong.indexer.repository;

import me.donggyeong.indexer.config.QuerydslConfig;
import me.donggyeong.indexer.entity.IndexingItem;
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
public class IndexingItemRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private IndexingItemRepository indexingItemRepository;

	@Test
	public void save() {
		// given
		Map<String, Object> documentBody = new HashMap<>();
		documentBody.put("category", "test-category");
		documentBody.put("title", "test-title");
		documentBody.put("description", "test-description");
		IndexingItem indexingItem = IndexingItem.builder()
			.action(Action.CREATE)
			.targetName("blog")
			.documentId(1L)
			.documentBody(documentBody)
			.build();

		// when
		IndexingItem savedIndexingItem = indexingItemRepository.save(indexingItem);

		// then
		assertThat(savedIndexingItem).isNotNull();
		assertThat(savedIndexingItem.getId()).isNotNull();
		assertThat(savedIndexingItem.getDocumentBody()).isEqualTo(documentBody);
		assertThat(savedIndexingItem.getConsumedAt()).isNotNull();
	}

	@Test
	public void findByConsumedAtAfterAndIsValidTrue() {
		// given
		Map<String, Object> documentBody = new HashMap<>();
		documentBody.put("category", "test-category");
		documentBody.put("title", "test-title");
		documentBody.put("description", "test-description");

		IndexingItem indexingItem1 = IndexingItem.builder()
			.action(Action.CREATE)
			.targetName("blog")
			.documentId(1L)
			.documentBody(documentBody)
			.build();

		IndexingItem indexingItem2 = IndexingItem.builder()
			.action(Action.UPDATE)
			.targetName("blog")
			.documentId(2L)
			.documentBody(documentBody)
			.build();

		entityManager.persist(indexingItem1);

		ZonedDateTime baseTime = ZonedDateTime.now();

		entityManager.persist(indexingItem2);

		entityManager.flush();

		// when
		List<IndexingItem> foundSourceData = indexingItemRepository.findByConsumedAtAfter(baseTime);

		// then
		assertThat(foundSourceData).hasSize(1);
		assertThat(foundSourceData).extracting(IndexingItem::getConsumedAt).allSatisfy(consumedAt ->
			assertThat(consumedAt).isAfter(baseTime)
		);
	}
}