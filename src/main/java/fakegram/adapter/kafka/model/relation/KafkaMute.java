package fakegram.adapter.kafka.model.relation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KafkaMute {

    private String muterId;
    private String muteTargetId;
    private boolean apply;

}
