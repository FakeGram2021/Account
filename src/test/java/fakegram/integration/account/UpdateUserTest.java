package fakegram.integration.account;

import fakegram.adapter.cassandra.repository.CassandraUserRepository;
import fakegram.adapter.http.dto.UpdateUserInfoDto;
import fakegram.container.AbstractContainerBaseTest;
import fakegram.domain.model.account.Gender;
import fakegram.domain.model.account.User;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import org.assertj.core.api.SoftAssertions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.UUID;

import static fakegram.domain.model.account.AccountPrivacy.PUBLIC;
import static io.micronaut.http.HttpRequest.PUT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class UpdateUserTest extends AbstractContainerBaseTest {

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
    public void accessingASecuredUrlWithoutAuthenticatingReturnsUnauthorized() {
        UpdateUserInfoDto updatedUserInfo = generateUpdateForUser();
        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class, () ->
            client.toBlocking().exchange(HttpRequest.PUT("/api/v1/account", updatedUserInfo)));

        assertEquals(exception.getStatus(), HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void itSuccessfullyUpdatesUser() {
        //Given
        User user = generateUser();
        userRepository.registerUser(user);

        UsernamePasswordCredentials creds = new UsernamePasswordCredentials("user3", "password123");
        HttpRequest request = HttpRequest.POST("/login", creds);
        HttpResponse<BearerAccessRefreshToken> rsp = client.toBlocking().exchange(request, BearerAccessRefreshToken.class);
        String accessToken = rsp.body().getAccessToken();


        UpdateUserInfoDto updateUserInfo = generateUpdateForUser();

        //When
        HttpResponse<Object> updateResponse = client.toBlocking().exchange(PUT("/api/v1/account", updateUserInfo).bearerAuth(accessToken));

        //Then
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(updateResponse.getStatus().toString()).isEqualTo(HttpStatus.NO_CONTENT.toString());
        softly.assertAll();
    }

    private User generateUser() {
        return User.builder()
                .accountId(UUID.randomUUID())
                .username("user3")
                .password("$2a$10$sSLsDYvnoKAnzvniTcOT7eGxE7TBqqnlPzxsBcu1xiwL0ceMAApxK")
                .biography("New boy in town")
                .dateOfBirth(2L)
                .email("abc@email.com")
                .firstName("John")
                .lastName("Doe")
                .gender(Gender.MALE)
                .privacy(PUBLIC)
                .phoneNumber("123")
                .webUrl("abc")
                .build();
    }

    public UpdateUserInfoDto generateUpdateForUser() {
        return UpdateUserInfoDto
                .builder()
                .biography("New biography")
                .phoneNumber("111-222")
                .build();
    }

}
