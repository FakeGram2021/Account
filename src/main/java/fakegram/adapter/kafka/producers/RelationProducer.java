package fakegram.adapter.kafka.producers;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface RelationProducer {

    @Topic("post_service_topic")
    void sendRelation(@KafkaKey String key, Object payload);

}
