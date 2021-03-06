package fakegram.domain.service;

import static fakegram.domain.model.account.AccountPrivacy.PUBLIC;
import static fakegram.domain.model.relation.RelationType.FOLLOW;
import static fakegram.domain.model.relation.RelationType.PENDING_FOLLOW;

import fakegram.adapter.cassandra.model.relation.RelationByObject;
import fakegram.adapter.cassandra.model.relation.RelationBySubject;
import fakegram.domain.message.handler.RelationMessageHandler;
import fakegram.domain.model.account.User;
import fakegram.domain.model.relation.RelationType;
import fakegram.domain.repository.RelationRepository;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FollowService {

    private final UserService userService;
    private final RelationMessageHandler relationMessageHandler;
    private final RelationRepository relationRepository;

    @Inject
    public FollowService(
        final UserService userService,
        final RelationMessageHandler relationMessageHandler,
        final RelationRepository relationRepository
    ) {
        this.userService = userService;
        this.relationMessageHandler = relationMessageHandler;
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

    public boolean followRelationExists(UUID accountId, UUID blockedId) {
        Optional<RelationBySubject> relation = relationRepository.findRelation(accountId, blockedId, FOLLOW);
        return relation.isPresent();
    }

    public void followUser(UUID followerId, UUID followeeId) {
        User followee = userService.findUserByAccountId(followeeId);
        if(followee.getPrivacy() == PUBLIC) {
            relationRepository.upsertRelation(followerId, followeeId, FOLLOW);
            relationMessageHandler.sendRelations(FOLLOW, followerId, followeeId, true);
        } else {
            relationRepository.upsertRelation(followerId, followeeId, PENDING_FOLLOW);
        }
    }

    public void unfollow(UUID followerId, UUID followeeId) {
        relationRepository.deleteRelation(followerId, followeeId, FOLLOW);
        relationMessageHandler.sendRelations(FOLLOW, followeeId, followeeId, false);
    }

    public void acceptFollowing(UUID followeeId, UUID followerId) {
        relationRepository.deleteRelation(followeeId, followerId, PENDING_FOLLOW);
        relationRepository.upsertRelation(followerId, followeeId, FOLLOW);
        relationMessageHandler.sendRelations(FOLLOW, followerId, followeeId, true);
    }

    public void declineFollowing(UUID followeeId, UUID followerId) {
       relationRepository.deleteRelation(followeeId, followerId, PENDING_FOLLOW);
    }

}
