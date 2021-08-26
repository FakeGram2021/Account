package fakegram.domain.repository;

import fakegram.domain.model.account.User;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface UserRepository {

    void registerUser(User account);

    User findByAccountId(UUID accountId);

    User findByUsername(String username);

    List<User> findByAccountIds(Collection<UUID> accountIds);

    void updateUser(User account);
}
