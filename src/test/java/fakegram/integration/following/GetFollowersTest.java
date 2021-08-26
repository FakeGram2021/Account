package fakegram.integration.following;

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
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static fakegram.domain.model.account.AccountPrivacy.PUBLIC;
import static io.micronaut.http.HttpRequest.GET;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class GetFollowersTest extends AbstractContainerBaseTest {

    private static HttpClient client;
    private static RelationRepository relationRepository;
    private static UserRepository userRepository;

    @BeforeClass
    public static void contextRunning() {
        userRepository = server
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
    public void accessingGetFollowersASecuredUrlWithoutAuthenticatingReturnsUnauthorized() {
        ArrayList<User> users = generateUsers();
        UUID accountId = users.get(0).getAccountId();
        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class, () ->
                client.toBlocking().exchange(HttpRequest.GET(String.format("/api/v1/relation/followers/accepted", accountId.toString()))));

        assertEquals(exception.getStatus(), HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void accessingGetFollowingASecuredUrlWithoutAuthenticatingReturnsUnauthorized() {
        ArrayList<User> users = generateUsers();
        UUID accountId = users.get(0).getAccountId();
        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class, () ->
                client.toBlocking().exchange(HttpRequest.GET(String.format("/api/v1/relation/following/accepted", accountId.toString()))));

        assertEquals(exception.getStatus(), HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void itSuccessfullyFollowsAnotherUser() {
        //Given
        ArrayList<User> users = generateUsers();
        insertUsersInRepository(users);
        UUID user1Id = users.get(0).getAccountId();
        UUID user2Id = users.get(1).getAccountId();
        UUID user3Id = users.get(2).getAccountId();
        insertFollowInRepository(user1Id, user2Id, user3Id);


        UsernamePasswordCredentials creds = new UsernamePasswordCredentials("user4", "password123");
        HttpRequest request = HttpRequest.POST("/login", creds);
        HttpResponse<BearerAccessRefreshToken> rsp = client.toBlocking().exchange(request, BearerAccessRefreshToken.class);
        String accessToken = rsp.body().getAccessToken();

        //When
        List<Object> followers = client
                .toBlocking()
                .retrieve(GET("/api/v1/relation/followers/accepted").bearerAuth(accessToken), List.class);

        //Then
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(followers).hasSize(1);
    }

    @Test
    public void itSuccessfullyUnfollowsAnotherUser() {
        //Given
        ArrayList<User> users = generateUsers();
        insertUsersInRepository(users);
        UUID user1Id = users.get(0).getAccountId();
        UUID user2Id = users.get(1).getAccountId();
        UUID user3Id = users.get(2).getAccountId();
        insertFollowInRepository(user1Id, user2Id, user3Id);

        UsernamePasswordCredentials creds = new UsernamePasswordCredentials("user3", "password123");
        HttpRequest request = HttpRequest.POST("/login", creds);
        HttpResponse<BearerAccessRefreshToken> rsp = client.toBlocking().exchange(request, BearerAccessRefreshToken.class);
        String accessToken = rsp.body().getAccessToken();

        //When
        HttpResponse<List> response = client
                .toBlocking()
                .exchange(GET("/api/v1/relation/following/accepted").bearerAuth(accessToken), List.class);
        Collection<Object> following = (Collection<Object>) response.getBody(List.class).get();


        //Then
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(response.getStatus().toString()).isEqualTo(HttpStatus.OK);
        softly.assertThat(following).hasSize(2);
    }

    private ArrayList<User> generateUsers() {
        ArrayList<User> users = new ArrayList<>();

        users.add(User.builder()
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
                .build());

        users.add(User.builder()
                .accountId(UUID.randomUUID())
                .username("user4")
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

        users.add(User.builder()
                .accountId(UUID.randomUUID())
                .username("user5")
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

    private void insertFollowInRepository(UUID user1, UUID user2, UUID user3) {
        relationRepository.upsertFollowingRelation(user1, user2, RequestStatus.ACCEPTED);
        relationRepository.upsertFollowerRelation(user2, user1, RequestStatus.ACCEPTED);

        relationRepository.upsertFollowingRelation(user1, user3, RequestStatus.ACCEPTED);
        relationRepository.upsertFollowerRelation(user3, user1, RequestStatus.ACCEPTED);
    }
}
