package fakegram.domain.service;

import fakegram.adapter.cassandra.model.relation.RelationByObject;
import fakegram.adapter.cassandra.model.relation.RelationBySubject;
import fakegram.domain.model.account.User;
import fakegram.domain.model.relation.RelationType;
import fakegram.domain.repository.RelationRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

import static fakegram.domain.model.account.AccountPrivacy.PUBLIC;
import static fakegram.domain.model.relation.RelationType.*;

@Singleton
public class FollowService {

    private final UserService userService;
    private final RelationRepository relationRepository;

    @Inject
    public FollowService(
        final UserService userService,
        final RelationRepository relationRepository
    ) {
        this.userService = userService;
        this.relationRepository = relationRepository;

    }

    public Collection<User> getFollowers(UUID accountId, RelationType relationType) {
        Collection<UUID> followersId = relationRepository.findAllRelationByObject(accountId, relationType)
                .stream()
                .map(RelationByObject::getSubjectId)
                .collect(Collectors.toList());
        return userService.findUsersByAccountIds(followersId);
    }

    public Collection<User> getFollowings(UUID accountId, RelationType relationType) {
        Collection<UUID> followeeIds = relationRepository.findAllRelationsBySubject(accountId, relationType)
                .stream()
                .map(RelationBySubject::getObjectId)
                .collect(Collectors.toList());
        return userService.findUsersByAccountIds(followeeIds);
    }

    public void followUser(UUID followerId, UUID followeeId) {
        User followee = userService.findUserByAccountId(followeeId);
        if(followee.getPrivacy() == PUBLIC) {
            relationRepository.upsertRelation(followerId, followeeId, FOLLOW);
        } else {
            relationRepository.upsertRelation(followerId, followeeId, PENDING_FOLLOW);
        }
    }

    public void unfollow(UUID followerId, UUID followeeId) {
        relationRepository.deleteRelation(followerId, followeeId, FOLLOW);
    }

    public void acceptFollowing(UUID followeeId, UUID followerId) {
        relationRepository.deleteRelation(followeeId, followerId, PENDING_FOLLOW);
        relationRepository.upsertRelation(followerId, followeeId, FOLLOW);
    }

    public void declineFollowing(UUID followeeId, UUID followerId) {
       relationRepository.deleteRelation(followeeId, followerId, PENDING_FOLLOW);
    }

}
