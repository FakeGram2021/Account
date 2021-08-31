package fakegram.adapter.http.dto;

import fakegram.domain.model.account.AccountPrivacy;
import fakegram.domain.model.account.Gender;
import fakegram.domain.model.account.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import static java.util.Objects.nonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserInfoDto {

    private String username;
    private String avatar;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String gender;
    private String privacy;
    private Long dateOfBirth;
    private String webUrl;
    private String biography;

    public User toUser(UUID accountId) {
        return User
                .builder()
                .accountId(accountId)
                .username(username)
                .avatar(avatar)
                .firstName(firstName)
                .lastName(lastName)
                .phoneNumber(phoneNumber)
                .email(email)
                .gender(nonNull(gender) ? Gender.valueOf(gender) : null)
                .privacy(nonNull(privacy) ? AccountPrivacy.valueOf(privacy) : null)
                .dateOfBirth(nonNull(dateOfBirth) ? dateOfBirth : 0)
                .webUrl(webUrl)
                .biography(biography)
                .build();
    }
}
