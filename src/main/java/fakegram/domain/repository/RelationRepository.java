package fakegram.domain.repository;

import fakegram.adapter.cassandra.model.following.Follower;
import fakegram.adapter.cassandra.model.following.Following;
import fakegram.domain.model.relation.RequestStatus;

import java.util.List;
import java.util.UUID;

public interface RelationRepository {

    void upsertFollowingRelation(UUID followerId, UUID followeeId, RequestStatus requestStatus);

    void upsertFollowerRelation(UUID followeeId, UUID followerId, RequestStatus requestStatus);

    void deleteFollowingRelation(UUID followerId, UUID followeeId, RequestStatus requestStatus);

    void deleteFollowerRelation(UUID followeeId, UUID followerId, RequestStatus requestStatus);

    List<Follower> findAllFollowers(UUID accountId, RequestStatus requestStatus);

    List<Following> findAllFollowing(UUID accountId, RequestStatus requestStatus);
}
