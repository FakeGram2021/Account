package fakegram.domain.service;

import fakegram.adapter.cassandra.model.relation.RelationBySubject;
import fakegram.domain.message.handler.RelationMessageHandler;
import fakegram.domain.model.account.User;
import fakegram.domain.repository.RelationRepository;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static fakegram.domain.model.relation.RelationType.MUTE;

public class MuteService {

    private final UserService userService;
    private final RelationMessageHandler relationMessageHandler;
    private final RelationRepository relationRepository;

    @Inject
    public MuteService(
        final UserService userService,
        final RelationMessageHandler relationMessageHandler,
        final RelationRepository relationRepository
    ) {
        this.userService = userService;
        this.relationMessageHandler = relationMessageHandler;
        this.relationRepository = relationRepository;
    }

    public Collection<User> getMutedUsers(UUID accountId) {
        List<UUID> mutedUsersIds = relationRepository
                .findAllRelationsBySubject(accountId, MUTE)
                .stream()
                .map(RelationBySubject::getObjectId)
                .collect(Collectors.toList());
        return userService.findUsersByAccountIds(mutedUsersIds);
    }

    public void muteUser(UUID accountId, UUID muteUserId) {
        relationRepository.upsertRelation(accountId, muteUserId, MUTE);
        relationMessageHandler.sendRelations(MUTE, accountId, muteUserId, true);
    }

    public void unmuteUser(UUID accountId, UUID muteUserId) {
        relationRepository.deleteRelation(accountId, muteUserId, MUTE);
        relationMessageHandler.sendRelations(MUTE, accountId, muteUserId, false);
    }
}
