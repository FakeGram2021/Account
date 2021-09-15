package fakegram.adapter.kafka.handlers;

import static fakegram.adapter.kafka.model.KafkaEvent.USER;
import static fakegram.adapter.kafka.model.KafkaEvent.USER_INFO;

import fakegram.adapter.kafka.model.KafkaMessage;
import fakegram.adapter.kafka.model.user.KafkaUser;
import fakegram.adapter.kafka.model.user.KafkaUserProduct;
import fakegram.adapter.kafka.producers.AgentProducer;
import fakegram.adapter.kafka.producers.UserProducer;
import fakegram.domain.message.handler.UserMessageHandler;
import fakegram.domain.model.account.User;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class KafkaUserMessageHandler implements UserMessageHandler {

    private final UserProducer userProducer;

    private final AgentProducer agentProducer;

    @Inject
    public KafkaUserMessageHandler(UserProducer userProducer, AgentProducer agentProducer) {
        this.userProducer = userProducer;
        this.agentProducer = agentProducer;
    }

    public void sendUser(final User user) {
        sendBasicUserMessage(user);
        sendProductInfoUserMessage(user);
    }

    public void sendBasicUserMessage(final User user) {
        KafkaUser kafkaUser = KafkaUser.from(user);
        KafkaMessage kafkaMessage = new KafkaMessage(USER, kafkaUser);
        userProducer.sendUser(kafkaMessage);
    }

    public void sendProductInfoUserMessage(final User user) {
        KafkaUserProduct kafkaUser = KafkaUserProduct.from(user);
        KafkaMessage kafkaMessage = new KafkaMessage(USER_INFO, kafkaUser);
        agentProducer.sendUser(kafkaMessage);
    }


}
