package fakegram.adapter.kafka.handlers;

import fakegram.adapter.kafka.model.KafkaMessage;
import fakegram.adapter.kafka.model.relation.KafkaBlock;
import fakegram.adapter.kafka.model.relation.KafkaFollow;
import fakegram.adapter.kafka.model.relation.KafkaMute;
import fakegram.adapter.kafka.producers.RelationProducer;
import fakegram.domain.message.handler.RelationMessageHandler;
import fakegram.domain.model.relation.RelationType;

import javax.inject.Singleton;
import java.util.UUID;

import static fakegram.adapter.kafka.model.KafkaEvent.*;

@Singleton
public class KafkaRelationMessageHandler implements RelationMessageHandler {

    private final RelationProducer relationProducer;

    public KafkaRelationMessageHandler(final RelationProducer relationProducer) {
        this.relationProducer = relationProducer;
    }

    @Override
    public void sendRelations(
        RelationType relationType,
        UUID subjectAccountId,
        UUID objectAccountId,
        boolean apply
    ) {
        Object payload;
        switch (relationType){
            case MUTE:
                payload = new KafkaMute(subjectAccountId.toString(), objectAccountId.toString(), apply);
                relationProducer.sendRelation(MUTE.toString(), payload);
                break;
            case BLOCK:
                payload = new KafkaBlock(subjectAccountId.toString(), objectAccountId.toString(), apply);
                relationProducer.sendRelation(BLOCK.toString(), payload);
                break;
            default:
                payload = new KafkaFollow(subjectAccountId.toString(), objectAccountId.toString(), apply);
                relationProducer.sendRelation(FOLLOW.toString(), payload);
        }
    }
}
