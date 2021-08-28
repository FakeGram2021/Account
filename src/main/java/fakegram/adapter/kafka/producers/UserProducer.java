package fakegram.adapter.kafka.producers;

import fakegram.adapter.kafka.model.KafkaMessage;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface UserProducer {

    @Topic("user")
    void sendUser(@KafkaKey String accountId, KafkaMessage message);

}
