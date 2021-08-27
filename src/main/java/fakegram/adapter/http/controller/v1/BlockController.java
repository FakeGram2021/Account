package fakegram.adapter.http.controller.v1;

import fakegram.domain.model.account.User;
import fakegram.domain.service.BlockService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;

import javax.inject.Inject;
import java.util.Collection;
import java.util.UUID;

import static io.micronaut.security.rules.SecurityRule.IS_AUTHENTICATED;

@Controller("/api/v1/block")
@Secured(IS_AUTHENTICATED)
public class BlockController {

    private final BlockService blockService;

    @Inject
    public BlockController(final BlockService blockService) {
        this.blockService = blockService;
    }

    @Get("")
    @Secured({"ROLE_USER"})
    public HttpResponse<Collection<User>> getBlockedUsers(Authentication authentication) {
        UUID accountID = UUID.fromString(authentication.getName());
        Collection<User> blockedUsers = blockService.getBlockedUsers(accountID);
        return HttpResponse.ok(blockedUsers);
    }

    @Post("/{blockUserId}")
    @Secured({"ROLE_USER"})
    public HttpResponse<Void> blockUser(@PathVariable UUID blockUserId, Authentication authentication) {
        UUID accountID = UUID.fromString(authentication.getName());
        blockService.blockUser(accountID, blockUserId);
        return HttpResponse.ok();
    }

    @Delete("/{blockUserId}")
    @Secured({"ROLE_USER"})
    public HttpResponse<Void> unblockUser(@PathVariable UUID blockUserId, Authentication authentication) {
        UUID accountID = UUID.fromString(authentication.getName());
        blockService.unblockUser(accountID, blockUserId);
        return HttpResponse.ok();
    }
}
