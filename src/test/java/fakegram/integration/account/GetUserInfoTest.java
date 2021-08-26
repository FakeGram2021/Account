package fakegram.integration.account;

import fakegram.adapter.cassandra.repository.CassandraUserRepository;
import fakegram.adapter.http.dto.UserInfoDto;
import fakegram.container.AbstractContainerBaseTest;
import fakegram.domain.model.account.Gender;
import fakegram.domain.model.account.User;
import io.micronaut.http.client.HttpClient;
import org.assertj.core.api.SoftAssertions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.UUID;

import static fakegram.domain.model.account.AccountPrivacy.PUBLIC;
import static io.micronaut.http.HttpRequest.GET;
import static java.lang.String.format;

public class GetUserInfoTest extends AbstractContainerBaseTest {


    private static HttpClient client;
    private static CassandraUserRepository userRepository;

    @BeforeClass
    public static void contextRunning() {
        userRepository = server
                .getApplicationContext()
                .getBean(CassandraUserRepository.class);
        client = server
                .getApplicationContext()
                .createBean(HttpClient.class, server.getURL());
    }

    @AfterClass
    public static void stopServer() {
        if (client != null) {
            client.stop();
        }
    }

    @Test
    public void itSuccessfullyGetsUserInfo() {
        //Given
        User user = generateUser();
        UUID userId = user.getAccountId();
        userRepository.registerUser(user);

        //When
        UserInfoDto persistedUser = client
                .toBlocking()
                .retrieve(GET(format("/api/v1/account/%s", userId.toString())), UserInfoDto.class);

        //Then
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(persistedUser.getUsername()).isEqualTo("user2");
        softly.assertAll();
    }

    private User generateUser() {
        return User.builder()
                .accountId(UUID.randomUUID())
                .username("user2")
                .password("password123")
                .biography("New boy in town")
                .dateOfBirth(872534603)
                .email("abc@email.com")
                .firstName("John")
                .lastName("Doe")
                .gender(Gender.MALE)
                .privacy(PUBLIC)
                .phoneNumber("123")
                .webUrl("abc")
                .build();
    }
}
