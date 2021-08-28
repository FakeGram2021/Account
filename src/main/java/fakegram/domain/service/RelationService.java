package fakegram.domain.service;

import fakegram.adapter.cassandra.model.following.Follower;
import fakegram.adapter.cassandra.model.following.Following;
import fakegram.domain.model.account.User;
import fakegram.domain.model.relation.RequestStatus;
import fakegram.domain.repository.RelationRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

import static fakegram.domain.model.account.AccountPrivacy.PUBLIC;
import static fakegram.domain.model.relation.RequestStatus.ACCEPTED;
import static fakegram.domain.model.relation.RequestStatus.PENDING;

@Singleton
public class RelationService {

    private UserService userService;
    private RelationRepository relationRepository;

    @Inject
    public RelationService(
        UserService userService,
        RelationRepository relationRepository
    ) {
        this.userService = userService;
        this.relationRepository = relationRepository;

    }

    public Collection<User> getFollowers(UUID accountId, RequestStatus requestStatus) {
        Collection<UUID> followersId = relationRepository.findAllFollowers(accountId, requestStatus)
                .stream()
                .map(Follower::getFollowerId)
                .collect(Collectors.toList());
        return userService.findUsersByAccountIds(followersId);
    }

    public Collection<User> getFollowings(UUID accountId, RequestStatus requestStatus) {
        Collection<UUID> followeeIds = relationRepository.findAllFollowing(accountId, requestStatus)
                .stream()
                .map(Following::getFolloweeId)
                .collect(Collectors.toList());
        return userService.findUsersByAccountIds(followeeIds);
    }

    public void followUser(UUID followerId, UUID followeeId) {
        User followee = userService.findUserByAccountId(followeeId);
        if(followee.getPrivacy() == PUBLIC) {
            relationRepository.upsertFollowingRelation(followerId, followeeId, ACCEPTED);
            relationRepository.upsertFollowerRelation(followeeId, followerId, ACCEPTED);
        } else {
            relationRepository.upsertFollowingRelation(followerId, followeeId, PENDING);
            relationRepository.upsertFollowerRelation(followeeId, followerId, PENDING);
        }
    }

    public void unfollow(UUID followerId, UUID followeeId) {
        relationRepository.deleteFollowingRelation(followerId, followeeId, ACCEPTED);
        relationRepository.deleteFollowerRelation(followeeId, followerId, ACCEPTED);
    }

    public void acceptFollowing(UUID followeeId, UUID followerId) {
        relationRepository.upsertFollowingRelation(followerId, followeeId, ACCEPTED);
        relationRepository.upsertFollowerRelation(followeeId, followerId, ACCEPTED);
    }

    public void declineFollowing(UUID followeeId, UUID followerId) {
       relationRepository.deleteFollowerRelation(followeeId, followerId, PENDING);
       relationRepository.deleteFollowingRelation(followerId, followeeId, PENDING);
    }

}
