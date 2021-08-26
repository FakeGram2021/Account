package fakegram.adapter.micronaut.security.provider;

import fakegram.domain.model.account.User;
import fakegram.domain.repository.UserRepository;
import fakegram.domain.security.PasswordEncoder;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.*;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

import static java.util.Objects.isNull;

@Singleton
public class AuthenticationProviderUserPassword implements AuthenticationProvider {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Inject
    public AuthenticationProviderUserPassword(
        final UserRepository userRepository,
        final PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public Publisher<AuthenticationResponse> authenticate(@Nullable HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {
        return Mono.create(emitter -> {
            String username = (String) authenticationRequest.getIdentity();
            String password = (String) authenticationRequest.getSecret();
            User user = userRepository.findByUsername(username);
            if(isNull(user)) {
                emitter.error(new AuthenticationException());
            }

            if (username.equals(user.getUsername()) && passwordEncoder.matches(password, user.getPassword())) {
                emitter.success(new UserDetails(user.getAccountId().toString(), List.of("ROLE_USER")));
            } else {
                emitter.error(new AuthenticationException());
            }
        });
    }
}
