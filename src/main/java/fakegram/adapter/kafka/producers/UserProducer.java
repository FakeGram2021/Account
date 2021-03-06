package fakegram.adapter.kafka.producers;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface UserProducer {

    @Topic("post_service_topic")
    void sendUser(Object payload);

}
