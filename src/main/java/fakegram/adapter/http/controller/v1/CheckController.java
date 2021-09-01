package fakegram.adapter.http.controller.v1;

import static io.micronaut.security.rules.SecurityRule.IS_AUTHENTICATED;

import fakegram.adapter.http.dto.CheckDTO;
import fakegram.domain.service.BlockService;
import fakegram.domain.service.FollowService;
import fakegram.domain.service.MuteService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import java.util.UUID;
import javax.inject.Inject;

@Controller("/api/v1/check-status")
@Secured(IS_AUTHENTICATED)
public class CheckController {

    private final FollowService followService;
    private final BlockService blockService;
    private final MuteService muteService;

    @Inject
    public CheckController(
        FollowService followService,
        BlockService blockService,
        MuteService muteService
    ) {
        this.followService = followService;
        this.blockService = blockService;
        this.muteService = muteService;
    }

    @Get("follow/{objectId}")
    public HttpResponse<CheckDTO> checkFollow(UUID objectId, Authentication authentication) {
        UUID accountID = UUID.fromString(authentication.getName());
        return HttpResponse.ok(new CheckDTO(followService.followRelationExists(accountID, objectId)));
    }

    @Get("block/{objectId}")
    public HttpResponse<CheckDTO> checkBlocked(UUID objectId, Authentication authentication) {
        UUID accountID = UUID.fromString(authentication.getName());
        return HttpResponse.ok(new CheckDTO(blockService.blockedRelationExists(accountID, objectId)));
    }

    @Get("mute/{objectId}")
    public HttpResponse<CheckDTO> checkMute(UUID objectId, Authentication authentication) {
        UUID accountID = UUID.fromString(authentication.getName());
        return HttpResponse.ok(new CheckDTO(muteService.muteRelationExists(accountID, objectId)));
    }
}
