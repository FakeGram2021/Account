package fakegram.adapter.http.controller.v1;

import static io.micronaut.security.rules.SecurityRule.IS_ANONYMOUS;

import fakegram.adapter.http.dto.SearchUserDto;
import fakegram.domain.model.account.User;
import fakegram.domain.service.SearchService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;

import io.micronaut.security.annotation.Secured;
import javax.inject.Inject;
import java.util.Collection;
import java.util.stream.Collectors;

@Controller("/api/v1/search")
@Secured(IS_ANONYMOUS)
public class SearchController {

    private final SearchService searchService;

    @Inject
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }


    @Get()
    public HttpResponse<Collection<SearchUserDto>> searchUsers(@QueryValue() String username) {
        Collection<User> users = searchService.searchByUsername(username);
        Collection<SearchUserDto> searchUsers = users.stream().map(SearchUserDto::from).collect(Collectors.toList());
        return HttpResponse.ok(searchUsers);
    }

}
