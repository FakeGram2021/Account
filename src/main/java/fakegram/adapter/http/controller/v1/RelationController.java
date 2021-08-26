package fakegram.adapter.http.controller.v1;

import fakegram.domain.model.account.User;
import fakegram.domain.model.relation.RequestStatus;
import fakegram.domain.service.RelationService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;

import javax.inject.Inject;
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
    public HttpResponse<Collection<User>> getFollowers(RequestStatus status, Authentication authentication) {
        UUID accountID = UUID.fromString(authentication.getName());
        Collection<User> followers = relationService.getFollowers(accountID, status);
        return HttpResponse.ok(followers);
    }

    @Get("/following/{status}")
    @Secured({"ROLE_USER"})
    public HttpResponse<Collection<User>> getFollowing(RequestStatus status, Authentication authentication) {
        UUID accountID = UUID.fromString(authentication.getName());
        Collection<User> followingUsers = relationService.getFollowings(accountID, status);
        return HttpResponse.ok(followingUsers);
    }

    @Post("/follow/{followeeId}")
    @Secured({"ROLE_USER"})
    public void followAccount(@PathVariable UUID followeeId, Authentication authentication) {
        UUID accountID = UUID.fromString(authentication.getName());
        relationService.followUser(accountID, followeeId);
    }

    @Put("/unfollow/{followingId}")
    @Secured({"ROLE_USER"})
    public void unfollowAccount(@PathVariable UUID followingId, Authentication authentication) {
        UUID accountID = UUID.fromString(authentication.getName());
        relationService.unfollow(accountID, followingId);
    }

    @Put("/follow/accept/{followingId}")
    @Secured({"ROLE_USER"})
    public void acceptFollow(@PathVariable UUID followingId, Authentication authentication) {
        UUID accountID = UUID.fromString(authentication.getName());
        relationService.acceptFollowing(accountID, followingId);
    }

    @Put("/follow/decline/{followingId}")
    @Secured({"ROLE_USER"})
    public void declineFollow(@PathVariable UUID followingId, Authentication authentication) {
        UUID accountID = UUID.fromString(authentication.getName());
        relationService.declineFollowing(accountID, followingId);
    }
}
