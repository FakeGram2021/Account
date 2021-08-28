package fakegram.adapter.cassandra.mapper.following;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace;
import com.datastax.oss.driver.api.mapper.annotations.DaoTable;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;
import fakegram.adapter.cassandra.dao.following.FollowerDao;
import fakegram.adapter.cassandra.dao.following.FollowingDao;

@Mapper
public interface RelationMapper {

    @DaoFactory
    FollowerDao followerDao(@DaoKeyspace CqlIdentifier keyspace, @DaoTable CqlIdentifier table);

    @DaoFactory
    FollowingDao followingDao(@DaoKeyspace CqlIdentifier keyspace, @DaoTable CqlIdentifier table);
}
