package fakegram.domain.repository;

import fakegram.domain.model.account.User;

import java.util.Collection;

public interface SearchRepository {

    Collection<User> searchByUsername(String username);
}
