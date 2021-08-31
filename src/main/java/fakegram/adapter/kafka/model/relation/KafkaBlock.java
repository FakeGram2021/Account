package fakegram.adapter.kafka.model.relation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KafkaBlock {

    private String blockerId;
    private String blockTarget;
    private boolean apply;

}
