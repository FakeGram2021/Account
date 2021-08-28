package fakegram.domain.service;

import fakegram.domain.message.handler.UserMessageHandler;
import fakegram.domain.model.account.User;
import fakegram.domain.repository.UserRepository;
import fakegram.domain.security.PasswordEncoder;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.UUID;

import static fakegram.domain.model.account.AccountPrivacy.PUBLIC;

@Singleton
public class UserService {

    private final UserMessageHandler userMessageHandler;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Inject
    public UserService(
        UserMessageHandler userMessageHandler,
        UserRepository userRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.userMessageHandler = userMessageHandler;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findUserByAccountId(UUID accountId) {
        return userRepository.findByAccountId(accountId);
    }

    public Collection<User> findUsersByAccountIds(Collection<UUID> accountIds) {
        return userRepository.findByAccountIds(accountIds);
    }

    public void updateUserInfo(User updatedUser) {
        User user = userRepository.findByAccountId(updatedUser.getAccountId());
        user.updateUserWith(updatedUser);
        userRepository.updateUser(user);
        userMessageHandler.sendUser(user);
    }

    public User registerUser(User user) {
        user.setPrivacy(PUBLIC);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.registerUser(user);
        userMessageHandler.sendUser(user);
        return user;
    }

}
