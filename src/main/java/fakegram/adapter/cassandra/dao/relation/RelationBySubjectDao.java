package fakegram.adapter.cassandra.dao.relation;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import com.datastax.oss.driver.api.mapper.annotations.Query;
import fakegram.adapter.cassandra.model.relation.RelationBySubject;

import java.util.UUID;

@Dao
public interface RelationBySubjectDao {

    @Insert
    void upsert(RelationBySubject relationBySubject);

    @Delete
    void delete(RelationBySubject relationBySubject);

    @Query("SELECT * FROM ${qualifiedTableId} WHERE subject_id = :accountId")
    PagingIterable<RelationBySubject> findAllObjectsInRelations(UUID accountId);
}
