package fakegram.adapter.http.dto;

import fakegram.domain.model.account.Gender;
import fakegram.domain.model.account.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewUserDto {

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private Gender gender;
    private long dateOfBirth;
    private String webUrl;
    private String biography;

    public User toUser() {
        return User
                .builder()
                .accountId(UUID.randomUUID())
                .username(username)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .phoneNumber(phoneNumber)
                .email(email)
                .gender(gender)
                .dateOfBirth(dateOfBirth)
                .webUrl(webUrl)
                .biography(biography)
                .build();
    }
}
