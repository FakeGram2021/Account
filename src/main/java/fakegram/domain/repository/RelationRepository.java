package fakegram.domain.repository;

import fakegram.adapter.cassandra.model.relation.RelationByObject;
import fakegram.adapter.cassandra.model.relation.RelationBySubject;
import fakegram.domain.model.relation.RelationType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RelationRepository {

    void upsertRelation(UUID followerId, UUID followeeId, RelationType relationType);

    void deleteRelation(UUID followerId, UUID followeeId, RelationType relationType);

    Optional<RelationBySubject> findRelation(UUID subjectId, UUID objectId, RelationType relationType);

    List<RelationByObject> findAllRelationByObject(UUID accountId, RelationType relationType);

    List<RelationBySubject> findAllRelationsBySubject(UUID accountId, RelationType relationType);
}
