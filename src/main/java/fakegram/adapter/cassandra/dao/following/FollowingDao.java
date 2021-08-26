package fakegram.adapter.cassandra.dao.following;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import com.datastax.oss.driver.api.mapper.annotations.Query;
import fakegram.adapter.cassandra.model.following.Following;

import java.util.UUID;

@Dao
public interface FollowingDao {

    @Insert
    void upsert(Following following);

    @Delete
    void delete(Following following);

    @Query("SELECT * FROM ${qualifiedTableId} WHERE follower_id = :accountId")
    PagingIterable<Following> findAllUserFollowers(UUID accountId);
}
