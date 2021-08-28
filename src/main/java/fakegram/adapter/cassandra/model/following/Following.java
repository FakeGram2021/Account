package fakegram.adapter.cassandra.model.following;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import fakegram.domain.model.relation.RequestStatus;
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
public class Following {

    @PartitionKey
    private UUID followerId;

    @ClusteringColumn()
    private UUID followeeId;

    private String requestStatus;

    public static Following from(UUID followerId, UUID followeeId, RequestStatus requestStatus) {
        return Following.
                builder()
                .followerId(followerId)
                .followeeId(followeeId)
                .requestStatus(requestStatus.toString())
                .build();
    }
}
