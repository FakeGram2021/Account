package fakegram.adapter.cassandra.repository;

import com.datastax.oss.driver.api.core.CqlSession;
import fakegram.adapter.cassandra.dao.following.FollowerDao;
import fakegram.adapter.cassandra.dao.following.FollowingDao;
import fakegram.adapter.cassandra.mapper.following.RelationMapper;
import fakegram.adapter.cassandra.mapper.following.RelationMapperBuilder;
import fakegram.adapter.cassandra.model.following.Follower;
import fakegram.adapter.cassandra.model.following.Following;
import fakegram.domain.model.relation.RequestStatus;
import fakegram.domain.repository.RelationRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.datastax.oss.driver.api.core.CqlIdentifier.fromCql;

public class CassandraRelationRepository implements RelationRepository {

    private static final String FOLLOWER_TABLE_NAME = "follower";
    private static final String FOLLOWING_TABLE_NAME = "following";

    private final FollowerDao followerDao;
    private final FollowingDao followingDao;

    @Inject
    public CassandraRelationRepository(final CqlSession session) {
        final RelationMapper relationMapper = new RelationMapperBuilder(session).build();
        followerDao = relationMapper.followerDao(
                fromCql("account"),
                fromCql(FOLLOWER_TABLE_NAME)
        );
        followingDao = relationMapper.followingDao(
                fromCql("account"),
                fromCql(FOLLOWING_TABLE_NAME)
        );
    }

    public void upsertFollowingRelation(UUID followerId, UUID followeeId, RequestStatus requestStatus) {
        Following followingRelation = Following.from(followerId, followeeId, requestStatus);
        followingDao.upsert(followingRelation);
    }

    public void upsertFollowerRelation(UUID followeeId, UUID followerId, RequestStatus requestStatus) {
        Follower followerRelation = Follower.from(followeeId, followerId, requestStatus);
        followerDao.upsert(followerRelation);
    }

    public void deleteFollowingRelation(UUID followerId, UUID followeeId, RequestStatus requestStatus) {
        Following followingRelation = Following.from(followerId, followeeId, requestStatus);
        followingDao.delete(followingRelation);
    }

    public void deleteFollowerRelation(UUID followeeId, UUID followerId, RequestStatus requestStatus) {
        Follower followerRelation = Follower.from(followeeId, followerId, requestStatus);
        followerDao.delete(followerRelation);
    }

    public List<Follower> findAllFollowers(UUID accountId, RequestStatus requestStatus) {
        return followerDao
                .findAllUserFollowers(accountId)
                .all()
                .stream()
                .filter(follower -> follower.getRequestStatus().equals(requestStatus.toString()))
                .collect(Collectors.toList());
    }

    public List<Following> findAllFollowing(UUID accountId, RequestStatus requestStatus) {
        return followingDao
                .findAllUserFollowers(accountId)
                .all()
                .stream()
                .filter(following -> following.getRequestStatus().equals(requestStatus.toString()))
                .collect(Collectors.toList());
    }
}
