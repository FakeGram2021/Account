package fakegram.domain.service;

import fakegram.domain.model.account.User;
import fakegram.domain.repository.SearchRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;

@Singleton
public class SearchService {

    private final SearchRepository searchRepository;

    @Inject
    public SearchService(SearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    public Collection<User> searchByUsername(String username) {
        return searchRepository.searchByUsername(username);
    }
}
