package fakegram.integration.following;

import fakegram.adapter.cassandra.model.following.Following;
import fakegram.container.AbstractContainerBaseTest;
import fakegram.domain.model.account.Gender;
import fakegram.domain.model.account.User;
import fakegram.domain.model.relation.RequestStatus;
import fakegram.domain.repository.RelationRepository;
import fakegram.domain.repository.UserRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static fakegram.domain.model.account.AccountPrivacy.PUBLIC;
import static io.micronaut.http.HttpRequest.POST;
import static io.micronaut.http.HttpRequest.PUT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class FollowingTest extends AbstractContainerBaseTest {

    private static HttpClient client;
    private static RelationRepository relationRepository;
    private static UserRepository userRepository;

    @BeforeClass
    public static void contextRunning() {
        userRepository =server
                .getApplicationContext()
                .getBean(UserRepository.class);
        relationRepository = server
                .getApplicationContext()
                .getBean(RelationRepository.class);
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
        ArrayList<User> users = generateUsers();
        UUID accountId = users.get(0).getAccountId();
        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class, () ->
                client.toBlocking().exchange(HttpRequest.POST(String.format("/api/v1/relation/follow/%s", accountId.toString()), "")));

        assertEquals(exception.getStatus(), HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void itSuccessfullyFollowsAnotherUser() throws InterruptedException {
        //Given
        ArrayList<User> users = generateUsers();
        insertUsersInRepository(users);
        UUID followeeId = users.get(1).getAccountId();

        UsernamePasswordCredentials creds = new UsernamePasswordCredentials("petar123", "password123");
        HttpRequest request = HttpRequest.POST("/login", creds);
        HttpResponse<BearerAccessRefreshToken> rsp = client.toBlocking().exchange(request, BearerAccessRefreshToken.class);
        String accessToken = rsp.body().getAccessToken();

        //When
        HttpResponse<Object> response = client
                .toBlocking()
                .exchange(POST(String.format("/api/v1/relation/follow/%s", followeeId.toString()), "").bearerAuth(accessToken));

        //Then
        List<Following> followers = relationRepository.findAllFollowing(users.get(0).getAccountId(), RequestStatus.ACCEPTED);
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(response.getStatus().toString()).isEqualTo(HttpStatus.OK);
        softly.assertThat(followers.get(0).getFollowerId()).isEqualTo(users.get(0).getAccountId());
        softly.assertThat(followers.get(0).getFolloweeId()).isEqualTo(users.get(1).getAccountId());
        softly.assertThat(followers).hasSize(1);
    }

    @Test
    public void itSuccessfullyUnfollowsAnotherUser() {
        //Given
        ArrayList<User> users = generateUsers();
        insertUsersInRepository(users);
        UUID followerId = users.get(0).getAccountId();
        UUID followeeId = users.get(1).getAccountId();
        insertFollowInRepository(followerId, followeeId);

        UsernamePasswordCredentials creds = new UsernamePasswordCredentials("petar123", "password123");
        HttpRequest request = HttpRequest.POST("/login", creds);
        HttpResponse<BearerAccessRefreshToken> rsp = client.toBlocking().exchange(request, BearerAccessRefreshToken.class);
        String accessToken = rsp.body().getAccessToken();

        //When
        HttpResponse<Object> response = client
                .toBlocking()
                .exchange(PUT(String.format("/api/v1/relation/unfollow/%s", followeeId.toString()), "").bearerAuth(accessToken));

        //Then
        List<Following> followers = relationRepository.findAllFollowing(users.get(0).getAccountId(), RequestStatus.ACCEPTED);
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(response.getStatus().toString()).isEqualTo(HttpStatus.OK);
        softly.assertThat(followers).hasSize(0);
    }

    private ArrayList<User> generateUsers() {
        ArrayList<User> users = new ArrayList<>();

        users.add(User.builder()
                .accountId(UUID.randomUUID())
                .username("petar123")
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
                .build());

        users.add(User.builder()
                .accountId(UUID.randomUUID())
                .username("mile")
                .password("$2a$10$sSLsDYvnoKAnzvniTcOT7eGxE7TBqqnlPzxsBcu1xiwL0ceMAApxK")
                .biography("New boy in town")
                .dateOfBirth(2L)
                .email("abc2@email.com")
                .firstName("Jane")
                .lastName("Doe")
                .gender(Gender.FEMALE)
                .privacy(PUBLIC)
                .phoneNumber("123")
                .webUrl("abc")
                .build());

        return users;
    }

    private void insertUsersInRepository(List<User> users) {
        users.forEach(userRepository::registerUser);
    }

    private void insertFollowInRepository(UUID followerId, UUID followeeId) {
        relationRepository.upsertFollowerRelation(followeeId, followerId, RequestStatus.ACCEPTED);
        relationRepository.upsertFollowingRelation(followerId, followeeId, RequestStatus.ACCEPTED);
    }
}
