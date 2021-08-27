package fakegram.adapter.http.controller.v1;

import fakegram.domain.model.account.User;
import fakegram.domain.model.relation.RelationType;
import fakegram.domain.service.FollowService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;

import javax.inject.Inject;
import java.util.Collection;
import java.util.UUID;

import static io.micronaut.security.rules.SecurityRule.IS_AUTHENTICATED;

@Controller("/api/v1")
@Secured(IS_AUTHENTICATED)
public class FollowController {

    private final FollowService followService;

    @Inject
    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @Get("/followers/{status}")
    @Secured({"ROLE_USER"})
    public HttpResponse<Collection<User>> getFollowers(RelationType status, Authentication authentication) {
        UUID accountID = UUID.fromString(authentication.getName());
        Collection<User> followers = followService.getFollowers(accountID, status);
        return HttpResponse.ok(followers);
    }

    @Get("/following/{status}")
    @Secured({"ROLE_USER"})
    public HttpResponse<Collection<User>> getFollowing(RelationType status, Authentication authentication) {
        UUID accountID = UUID.fromString(authentication.getName());
        Collection<User> followingUsers = followService.getFollowings(accountID, status);
        return HttpResponse.ok(followingUsers);
    }

    @Post("/follow/{followeeId}")
    @Secured({"ROLE_USER"})
    public void followAccount(@PathVariable UUID followeeId, Authentication authentication) {
        UUID accountID = UUID.fromString(authentication.getName());
        followService.followUser(accountID, followeeId);
    }

    @Put("/unfollow/{followingId}")
    @Secured({"ROLE_USER"})
    public void unfollowAccount(@PathVariable UUID followingId, Authentication authentication) {
        UUID accountID = UUID.fromString(authentication.getName());
        followService.unfollow(accountID, followingId);
    }

    @Put("/follow/accept/{followingId}")
    @Secured({"ROLE_USER"})
    public void acceptFollow(@PathVariable UUID followingId, Authentication authentication) {
        UUID accountID = UUID.fromString(authentication.getName());
        followService.acceptFollowing(accountID, followingId);
    }

    @Put("/follow/decline/{followingId}")
    @Secured({"ROLE_USER"})
    public void declineFollow(@PathVariable UUID followingId, Authentication authentication) {
        UUID accountID = UUID.fromString(authentication.getName());
        followService.declineFollowing(accountID, followingId);
    }
}
