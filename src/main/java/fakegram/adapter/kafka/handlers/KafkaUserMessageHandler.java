package fakegram.adapter.kafka.handlers;

import static fakegram.adapter.kafka.model.KafkaEvent.USER;
import static fakegram.adapter.kafka.model.KafkaEvent.USER_INFO;

import fakegram.adapter.kafka.model.user.KafkaUser;
import fakegram.adapter.kafka.model.user.KafkaUserProduct;
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

    public void sendUser(final User user) {
        sendBasicUserMessage(user);
        sendProductInfoUserMessage(user);
    }

    public void sendBasicUserMessage(final User user) {
        KafkaUser kafkaUser = KafkaUser.from(user);
        userProducer.sendUser(USER.toString(), kafkaUser);
    }

    public void sendProductInfoUserMessage(final User user) {
        KafkaUserProduct kafkaUser = KafkaUserProduct.from(user);
        userProducer.sendUser(USER_INFO.toString(), kafkaUser);
    }


}
