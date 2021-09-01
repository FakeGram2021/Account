package fakegram.adapter.http.controller.v1;


import fakegram.adapter.http.dto.NewUserDto;
import fakegram.adapter.http.dto.UserInfoDto;
import fakegram.domain.model.account.User;
import fakegram.domain.service.UserService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;

import javax.inject.Inject;

import static io.micronaut.http.MediaType.APPLICATION_JSON;

@Controller("/api/v1/agent")
@Secured({"ROLE_ADMIN"})
public class AgentController {

    private final UserService userService;

    @Inject
    public AgentController(UserService userService) {
        this.userService = userService;
    }

    @Post(consumes = APPLICATION_JSON)
    public HttpResponse<UserInfoDto> registerAgent(@Body NewUserDto newUser) {
        User persistedUser = userService.registerAgent(newUser.toUser());
        return HttpResponse.created(UserInfoDto.from(persistedUser));
    }
}
