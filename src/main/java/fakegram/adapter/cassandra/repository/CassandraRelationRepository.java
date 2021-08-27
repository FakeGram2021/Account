package fakegram.adapter.cassandra.repository;

import com.datastax.oss.driver.api.core.CqlSession;
import fakegram.adapter.cassandra.dao.relation.RelationByObjectDao;
import fakegram.adapter.cassandra.dao.relation.RelationBySubjectDao;
import fakegram.adapter.cassandra.mapper.relation.RelationMapper;
import fakegram.adapter.cassandra.mapper.relation.RelationMapperBuilder;
import fakegram.adapter.cassandra.model.relation.RelationByObject;
import fakegram.adapter.cassandra.model.relation.RelationBySubject;
import fakegram.domain.model.relation.RelationType;
import fakegram.domain.repository.RelationRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.datastax.oss.driver.api.core.CqlIdentifier.fromCql;

public class CassandraRelationRepository implements RelationRepository {

    private static final String KEYSPACE = "account";
    private static final String RELATION_BY_OBJECT_TABLE = "relation_by_object";
    private static final String RELATION_BY_SUBJECT_TABLE = "relation_by_subject";

    private final RelationByObjectDao relationByObjectDao;
    private final RelationBySubjectDao relationBySubjectDao;

    @Inject
    public CassandraRelationRepository(final CqlSession session) {
        final RelationMapper relationMapper = new RelationMapperBuilder(session).build();
        relationByObjectDao = relationMapper.relationByObjectDao(
                fromCql(KEYSPACE),
                fromCql(RELATION_BY_OBJECT_TABLE)
        );
        relationBySubjectDao = relationMapper.relationBySubjectDao(
                fromCql(KEYSPACE),
                fromCql(RELATION_BY_SUBJECT_TABLE)
        );
    }

    public void upsertRelation(UUID subject, UUID object, RelationType relationType) {
        this.upsertRelationBySubject(subject, object, relationType);
        this.upsertRelationByObject(object, subject, relationType);
    }

    public void deleteRelation(UUID subjectId, UUID objectId, RelationType relationType) {
        this.deleteRelationBySubject(subjectId, objectId, relationType);
        this.deleteRelationByObject(objectId, subjectId, relationType);
    }

    public List<RelationByObject> findAllRelationByObject(UUID accountId, RelationType relationType) {
        return relationByObjectDao
                .findAllSubjectInRelations(accountId)
                .all()
                .stream()
                .filter(follower -> follower.getRelationType().equals(relationType.toString()))
                .collect(Collectors.toList());
    }

    public List<RelationBySubject> findAllRelationsBySubject(UUID accountId, RelationType relationType) {
        return relationBySubjectDao
                .findAllObjectsInRelations(accountId)
                .all()
                .stream()
                .filter(following -> following.getRelationType().equals(relationType.toString()))
                .collect(Collectors.toList());
    }

    private void upsertRelationBySubject(UUID subjectId, UUID objectId, RelationType relationType) {
        RelationBySubject relationBySubjectRelation = RelationBySubject.from(subjectId, objectId, relationType);
        relationBySubjectDao.upsert(relationBySubjectRelation);
    }

    private void upsertRelationByObject(UUID objectId, UUID subjectId, RelationType relationType) {
        RelationByObject relationByObjectRelation = RelationByObject.from(objectId, subjectId, relationType);
        relationByObjectDao.upsert(relationByObjectRelation);
    }

    private void deleteRelationBySubject(UUID subjectId, UUID objectId, RelationType relationType) {
        RelationBySubject relationBySubjectRelation = RelationBySubject.from(subjectId, objectId, relationType);
        relationBySubjectDao.delete(relationBySubjectRelation);
    }

    private void deleteRelationByObject(UUID objectId, UUID subjectId, RelationType relationType) {
        RelationByObject relationByObjectRelation = RelationByObject.from(objectId, subjectId, relationType);
        relationByObjectDao.delete(relationByObjectRelation);
    }
}
