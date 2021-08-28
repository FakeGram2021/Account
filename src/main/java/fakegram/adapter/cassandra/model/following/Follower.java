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
public class Follower {

    @PartitionKey
    private UUID followeeId;

    @ClusteringColumn()
    private UUID followerId;

    private String requestStatus;

    public static Follower from(UUID followeeId, UUID followerId, RequestStatus requestStatus) {
        return Follower
                .builder()
                .followeeId(followeeId)
                .followerId(followerId)
                .requestStatus(requestStatus.toString())
                .build();
    }
}
