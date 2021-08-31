package fakegram.adapter.http.controller.v1;

import fakegram.domain.model.account.User;
import fakegram.domain.service.MuteService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;

import javax.inject.Inject;
import java.util.Collection;
import java.util.UUID;

import static io.micronaut.security.rules.SecurityRule.IS_AUTHENTICATED;

@Controller("/api/v1/mute")
@Secured(IS_AUTHENTICATED)
public class MuteController {

    private final MuteService muteService;

    @Inject
    public MuteController(final MuteService muteService) {
        this.muteService = muteService;
    }

    @Get("")
    @Secured({"ROLE_USER"})
    public HttpResponse<Collection<User>> getMutedUsers(Authentication authentication) {
        UUID accountID = UUID.fromString(authentication.getName());
        Collection<User> mutedUsers = muteService.getMutedUsers(accountID);
        return HttpResponse.ok(mutedUsers);
    }

    @Post("/{muteUserId}")
    @Secured({"ROLE_USER"})
    public HttpResponse<?> muteUser(@PathVariable UUID muteUserId, Authentication authentication) {
        UUID accountID = UUID.fromString(authentication.getName());
        muteService.muteUser(accountID, muteUserId);
        return HttpResponse.ok();
    }

    @Delete("/{muteUserId}")
    @Secured({"ROLE_USER"})
    public HttpResponse<?> unmuteUser(@PathVariable UUID muteUserId, Authentication authentication) {
        UUID accountID = UUID.fromString(authentication.getName());
        muteService.unmuteUser(accountID, muteUserId);
        return HttpResponse.ok();
    }


}
