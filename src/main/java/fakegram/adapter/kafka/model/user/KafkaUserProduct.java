package fakegram.adapter.kafka.model.user;

import fakegram.domain.model.account.User;
import java.time.ZoneId;
import java.util.Date;
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
                .birthYear(new Date(user.getDateOfBirth()).toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate().getYear())
                .build();
    }

}
