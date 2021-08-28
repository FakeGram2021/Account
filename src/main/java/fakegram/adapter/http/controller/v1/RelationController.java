package fakegram.adapter.http.controller.v1;

import fakegram.domain.model.account.User;
import fakegram.domain.model.relation.RequestStatus;
import fakegram.domain.service.RelationService;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;

import javax.inject.Inject;
import java.security.Principal;
import java.util.Collection;
import java.util.UUID;

import static io.micronaut.security.rules.SecurityRule.IS_AUTHENTICATED;

@Controller("/api/v1/relation")
@Secured(IS_AUTHENTICATED)
public class RelationController {

    private final RelationService relationService;

    @Inject
    public RelationController(RelationService relationService) {
        this.relationService = relationService;
    }

    @Get("/followers/{status}")
    @Secured({"ROLE_USER"})
    public Collection<User> getFollowers(RequestStatus status, Principal principal) {
        UUID accountID = UUID.fromString(principal.getName());
        return relationService.getFollowers(accountID, status);
    }

    @Get("/following/{status}")
    @Secured({"ROLE_USER"})
    public Collection<User> getFollowing(RequestStatus status, Principal principal) {
        UUID accountID = UUID.fromString(principal.getName());
        return relationService.getFollowings(accountID, status);
    }

    @Post("/follow/{followeeId}")
    @Secured({"ROLE_USER"})
    public void followAccount(@PathVariable UUID followeeId, Principal principal) {
        UUID accountID = UUID.fromString(principal.getName());
        relationService.followUser(accountID, followeeId);
    }

    @Put("/unfollow/{followingId}")
    @Secured({"ROLE_USER"})
    public void unfollowAccount(@PathVariable UUID followingId, Principal principal) {
        UUID accountID = UUID.fromString(principal.getName());
        relationService.unfollow(accountID, followingId);
    }

    @Put("/follow/accept/{followingId}")
    @Secured({"ROLE_USER"})
    public void acceptFollow(@PathVariable UUID followingId, Principal principal) {
        UUID accountID = UUID.fromString(principal.getName());
        relationService.acceptFollowing(accountID, followingId);
    }

    @Put("/follow/decline/{followingId}")
    @Secured({"ROLE_USER"})
    public void declineFollow(@PathVariable UUID followingId, Principal principal) {
        UUID accountID = UUID.fromString(principal.getName());
        relationService.declineFollowing(accountID, followingId);
    }
}
