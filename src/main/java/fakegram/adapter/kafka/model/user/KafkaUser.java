package fakegram.adapter.kafka.model.user;

import fakegram.domain.model.account.AccountPrivacy;
import fakegram.domain.model.account.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KafkaUser {

    private String id;
    private String username;
    private String userAvatar;
    private boolean publicAccount;

    public static KafkaUser from(User user) {
        return KafkaUser
                .builder()
                .id(user.getAccountId().toString())
                .username(user.getUsername())
                .userAvatar(user.getAvatar())
                .publicAccount(user.getPrivacy() == AccountPrivacy.PUBLIC)
                .build();
    }

}
