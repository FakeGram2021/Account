package fakegram.adapter.kafka.handlers;

import fakegram.adapter.kafka.model.KafkaEvent;
import fakegram.adapter.kafka.model.KafkaMessage;
import fakegram.adapter.kafka.model.user.KafkaUser;
import fakegram.adapter.kafka.producers.UserProducer;
import fakegram.domain.message.handler.UserMessageHandler;
import fakegram.domain.model.account.User;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class KafkaUserMessageHandler implements UserMessageHandler {

    private final UserProducer userProducer;

    @Inject
    public KafkaUserMessageHandler(UserProducer userProducer) {
        this.userProducer = userProducer;
    }

    public void sendUser(User user) {
        KafkaUser kafkaUser = KafkaUser.from(user);
        KafkaMessage message = new KafkaMessage(KafkaEvent.USER, kafkaUser);
        userProducer.sendUser(user.getAccountId().toString(), message);
    }


}
