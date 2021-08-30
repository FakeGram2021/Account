package fakegram.integration.account;

import fakegram.adapter.cassandra.repository.CassandraUserRepository;
import fakegram.adapter.http.dto.NewUserDto;
import fakegram.adapter.http.dto.UserInfoDto;
import fakegram.container.AbstractContainerBaseTest;
import fakegram.domain.model.account.Gender;
import io.micronaut.http.client.HttpClient;
import org.assertj.core.api.SoftAssertions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.micronaut.http.HttpRequest.POST;

public class RegisterUserTest extends AbstractContainerBaseTest {

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
    public void itSuccessfullyRegistersUser() {
        //Given
        NewUserDto newUser = generateUser();

        //When
        UserInfoDto persistedUser = client.toBlocking().retrieve(POST("/api/v1/account", newUser), UserInfoDto.class);

        //Then
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(persistedUser.getUsername()).isEqualTo("user1");
        softly.assertAll();
    }

    private NewUserDto generateUser() {
        return NewUserDto.builder()
                .username("user1")
                .password("password123")
                .biography("New boy in town")
                .dateOfBirth(872534603)
                .email("abc@email.com")
                .firstName("John")
                .lastName("Doe")
                .gender(Gender.MALE)
                .phoneNumber("123")
                .webUrl("abc")
                .build();
    }
}
