package fakegram.domain.model.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.isNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private UUID accountId;
    private String avatar;
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
    private AccountPrivacy privacy;
    private boolean isAdmin;
    private boolean isAgent;
    private List<User> following;
    private List<User> followedBy;
    private List<User> blocked;
    private List<User> muted;

    public void updateUserWith(User updatedUser) {
        username = isNull(updatedUser.getUsername()) ? username : updatedUser.getUsername();
        avatar = isNull(updatedUser.getAvatar()) ? avatar : updatedUser.getAvatar();
        firstName = isNull(updatedUser.getFirstName()) ? firstName : updatedUser.getFirstName();
        lastName = isNull(updatedUser.getLastName()) ? lastName : updatedUser.getLastName();
        phoneNumber = isNull(updatedUser.getPhoneNumber()) ? phoneNumber : updatedUser.getPhoneNumber();
        email = isNull(updatedUser.getEmail()) ? email : updatedUser.getEmail();
        gender = isNull(updatedUser.getGender()) ? gender : updatedUser.getGender();
        dateOfBirth = updatedUser.getDateOfBirth() == 0 ?  dateOfBirth : updatedUser.getDateOfBirth();
        biography = isNull(updatedUser.getBiography()) ? biography : updatedUser.getBiography();
        privacy = isNull(updatedUser.getPrivacy()) ? privacy : updatedUser.getPrivacy();
    }
}
