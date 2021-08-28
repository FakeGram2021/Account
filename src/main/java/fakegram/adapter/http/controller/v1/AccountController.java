package fakegram.adapter.http.controller.v1;

import fakegram.adapter.http.dto.NewUserDto;
import fakegram.adapter.http.dto.UpdateUserInfoDto;
import fakegram.adapter.http.dto.UserInfoDto;
import fakegram.domain.model.account.User;
import fakegram.domain.service.UserService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;

import javax.inject.Inject;
import java.util.UUID;

import static io.micronaut.http.MediaType.APPLICATION_JSON;
import static io.micronaut.security.rules.SecurityRule.IS_ANONYMOUS;
import static io.micronaut.security.rules.SecurityRule.IS_AUTHENTICATED;

@Controller("/api/v1/account")
@Secured(IS_AUTHENTICATED)
public class AccountController {

    private final UserService userService;

    @Inject
    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @Secured(IS_ANONYMOUS)
    @Get("/{id}")
    public HttpResponse<UserInfoDto> getAccountInfo(@PathVariable(name = "id") UUID accountId
    ) {
        User user = userService.findUserByAccountId(accountId);
        return HttpResponse.ok(UserInfoDto.from(user));
    }

    @Secured(IS_ANONYMOUS)
    @Post(consumes = APPLICATION_JSON)
    public HttpResponse<UserInfoDto> registerNewUser(@Body NewUserDto newUser) {
        User persistedUser = userService.registerUser(newUser.toUser());
        return HttpResponse.created(UserInfoDto.from(persistedUser));
    }

    @Put(consumes = APPLICATION_JSON)
    public HttpResponse<?> updateAccount(
        @Body UpdateUserInfoDto updatedUser,
        Authentication authentication
    ) {
        UUID accountId = UUID.fromString(authentication.getName());
        User user = updatedUser.toUser(accountId);
        userService.updateUserInfo(user);
        return HttpResponse.noContent();
    }

}
