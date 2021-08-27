package fakegram.adapter.cassandra.dao.relation;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import com.datastax.oss.driver.api.mapper.annotations.Query;
import fakegram.adapter.cassandra.model.relation.RelationByObject;

import java.util.UUID;

@Dao
public interface RelationByObjectDao {

    @Insert
    void upsert(RelationByObject relationByObject);

    @Delete
    void delete(RelationByObject relationByObject);

    @Query("SELECT * FROM ${qualifiedTableId} WHERE object_id = :accountId")
    PagingIterable<RelationByObject> findAllSubjectInRelations(UUID accountId);

}
