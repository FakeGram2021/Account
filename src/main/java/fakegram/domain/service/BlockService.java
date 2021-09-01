package fakegram.domain.service;

import fakegram.adapter.cassandra.model.relation.RelationBySubject;
import fakegram.domain.message.handler.RelationMessageHandler;
import fakegram.domain.model.account.User;
import fakegram.domain.repository.RelationRepository;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static fakegram.domain.model.relation.RelationType.BLOCK;

public class BlockService {

    private final UserService userService;
    private final RelationMessageHandler relationMessageHandler;
    private final RelationRepository relationRepository;

    @Inject
    public BlockService(
        final UserService userService,
        final RelationMessageHandler relationMessageHandler,
        final RelationRepository relationRepository
    ) {
        this.userService = userService;
        this.relationMessageHandler = relationMessageHandler;
        this.relationRepository = relationRepository;
    }

    public Collection<User> getBlockedUsers(UUID accountId) {
        List<UUID> blockedUsersId = relationRepository
            .findAllRelationsBySubject(accountId, BLOCK)
            .stream()
            .map(RelationBySubject::getObjectId)
            .collect(Collectors.toList());
        return userService.findUsersByAccountIds(blockedUsersId);
    }

    public boolean blockedRelationExists(UUID accountId, UUID blockedId) {
        Optional<RelationBySubject> relation = relationRepository.findRelation(accountId, blockedId, BLOCK);
        return relation.isPresent();
    }

    public void blockUser(UUID accountId, UUID blockUserId) {
        relationRepository.upsertRelation(accountId, blockUserId, BLOCK);
        relationMessageHandler.sendRelations(BLOCK, accountId, blockUserId, true);
    }

    public void unblockUser(UUID accountId, UUID blockedUserId) {
        relationRepository.deleteRelation(accountId, blockedUserId, BLOCK);
        relationMessageHandler.sendRelations(BLOCK, accountId, blockedUserId, false);
    }
}
