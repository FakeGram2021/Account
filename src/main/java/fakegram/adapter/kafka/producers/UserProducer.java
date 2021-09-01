package fakegram.adapter.kafka.producers;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface UserProducer {

    @Topic("post_service_topic")
    void sendUser(@KafkaKey String key, Object payload);

    @Topic("agent_service_topic")
    void sendUserProduct(@KafkaKey String key, Object payload);

}
