package me.donggyeong.indexer.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import me.donggyeong.indexer.dto.SourceDataRequest;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {
	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapAddress;

	@Value("${spring.kafka.consumer.group-id}")
	private String groupId;

	@Bean
	public ConsumerFactory<String, SourceDataRequest> pushEntityConsumerFactory() {
		JsonDeserializer<SourceDataRequest> deserializer = gcmPushEntityJsonDeserializer();
		return new DefaultKafkaConsumerFactory<>(
			consumerFactoryConfig(deserializer),
			new StringDeserializer(),
			deserializer);
	}

	private Map<String, Object> consumerFactoryConfig(JsonDeserializer<SourceDataRequest> deserializer) {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
		return props;
	}

	private JsonDeserializer<SourceDataRequest> gcmPushEntityJsonDeserializer() {
		JsonDeserializer<SourceDataRequest> deserializer = new JsonDeserializer<>(SourceDataRequest.class);
		deserializer.setRemoveTypeHeaders(false);
		deserializer.setUseTypeMapperForKey(true);
		deserializer.addTrustedPackages("*");
		return deserializer;
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, SourceDataRequest> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, SourceDataRequest> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(pushEntityConsumerFactory());
		return factory;
	}
}