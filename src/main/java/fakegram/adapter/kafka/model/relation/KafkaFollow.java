package fakegram.adapter.kafka.model.relation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KafkaFollow {

    private String followerId;
    private String followTargetId;
    private boolean apply;

}
