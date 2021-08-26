package fakegram.adapter.http.dto;

import fakegram.domain.model.account.AccountPrivacy;
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
public class UserInfoDto {

    private UUID accountId;
    private String username;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private Gender gender;
    private long dateOfBirth;
    private String webUrl;
    private String biography;
    private AccountPrivacy privacy;

    public static UserInfoDto from(User user) {
        return UserInfoDto.builder()
                .accountId(user.getAccountId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .dateOfBirth(user.getDateOfBirth())
                .webUrl(user.getWebUrl())
                .biography(user.getBiography())
                .privacy(user.getPrivacy())
                .build();
    }
}
