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
    public static final String ACCOUNT_ID = "account_id";
    public static final String PASSWORD = "password";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String EMAIL = "email";
    public static final String GENDER = "gender";
    public static final String DATE_OF_BIRTH = "date_of_birth";
    public static final String WEB_URL = "web_url";
    public static final String BIOGRAPHY = "biography";
    public static final String PRIVACY = "privacy";


    @PartitionKey
    private UUID accountId;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String gender;
    private String dateOfBirth;
    private String webUrl;
    private String biography;
    private String privacy;

    public static AccountInfo from(User user) {
        return AccountInfo.builder()
                .accountId(user.getAccountId())
                .username(user.getUsername())
                .password(user.getPassword())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .gender(user.getGender().toString())
                .dateOfBirth(Long.toString(user.getDateOfBirth()))
                .webUrl(user.getWebUrl())
                .biography(user.getBiography())
                .privacy(user.getPrivacy().toString())
                .build();
    }

    public User toUser() {
        return User.builder()
                .accountId(accountId)
                .username(username)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .phoneNumber(phoneNumber)
                .email(email)
                .gender(Gender.valueOf(gender))
                .dateOfBirth(Long.parseLong(dateOfBirth))
                .webUrl(webUrl)
                .biography(biography)
                .privacy(AccountPrivacy.valueOf(privacy))
                .build();
    }

}
