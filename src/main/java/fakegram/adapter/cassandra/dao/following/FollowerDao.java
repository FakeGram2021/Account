package fakegram.adapter.cassandra.dao.following;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import com.datastax.oss.driver.api.mapper.annotations.Query;
import fakegram.adapter.cassandra.model.following.Follower;

import java.util.UUID;

@Dao
public interface FollowerDao {

    @Insert
    void upsert(Follower follower);

    @Delete
    void delete(Follower follower);

    @Query("SELECT * FROM ${qualifiedTableId} WHERE followee_id = :accountId")
    PagingIterable<Follower> findAllUserFollowers(UUID accountId);

}
