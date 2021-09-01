package fakegram.adapter.cassandra.model;

import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import fakegram.domain.model.account.AccountPrivacy;
import fakegram.domain.model.account.Gender;
import fakegram.domain.model.account.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountInfo {

    public static final String TABLE_BY_ACCOUNT_ID_NAME = "account_info_by_account_id";

    @PartitionKey
    private UUID accountId;
    private String username;
    private String password;
    private String avatar;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String gender;
    private String dateOfBirth;
    private String webUrl;
    private String biography;
    private String privacy;
    private String isAgent;
    private String isAdmin;

    public static AccountInfo from(User user) {
        return AccountInfo.builder()
                .accountId(user.getAccountId())
                .username(user.getUsername())
                .password(user.getPassword())
                .avatar(user.getAvatar())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .gender(user.getGender().toString())
                .dateOfBirth(Long.toString(user.getDateOfBirth()))
                .webUrl(user.getWebUrl())
                .biography(user.getBiography())
                .privacy(user.getPrivacy().toString())
                .isAdmin(Boolean.toString(user.isAdmin()))
                .isAgent(Boolean.toString(user.isAgent()))
                .build();
    }

    public User toUser() {
        return User.builder()
                .accountId(accountId)
                .username(username)
                .password(password)
                .avatar(avatar)
                .firstName(firstName)
                .lastName(lastName)
                .phoneNumber(phoneNumber)
                .email(email)
                .gender(Gender.valueOf(gender))
                .dateOfBirth(Long.parseLong(dateOfBirth))
                .webUrl(webUrl)
                .biography(biography)
                .privacy(AccountPrivacy.valueOf(privacy))
                .isAdmin(Boolean.parseBoolean(isAdmin))
                .isAgent(Boolean.parseBoolean(isAgent))
                .build();
    }

}
