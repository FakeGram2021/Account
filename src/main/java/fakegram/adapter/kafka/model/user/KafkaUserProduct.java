package fakegram.adapter.kafka.model.user;

import fakegram.domain.model.account.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KafkaUserProduct {

    private String id;
    private String sex;
    private long birthYear;

    public static KafkaUserProduct from(User user) {
        return KafkaUserProduct
                .builder()
                .id(user.getAccountId().toString())
                .sex(user.getGender().toString())
                .birthYear(user.getDateOfBirth())
                .build();
    }

}
