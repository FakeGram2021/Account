package fakegram.domain.message.handler;

import fakegram.domain.model.relation.RelationType;

import java.util.UUID;

public interface RelationMessageHandler {

    void sendRelations(RelationType relationType, UUID subjectAccountId, UUID objectAccountId, boolean apply);
}
