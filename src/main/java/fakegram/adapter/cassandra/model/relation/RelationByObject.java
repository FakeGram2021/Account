package fakegram.adapter.cassandra.model.relation;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import fakegram.domain.model.relation.RelationType;
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
public class RelationByObject {

    @PartitionKey
    private UUID objectId;

    @ClusteringColumn(1)
    private String relationType;

    @ClusteringColumn(2)
    private UUID subjectId;

    public static RelationByObject from(UUID followeeId, UUID followerId, RelationType relationType) {
        return RelationByObject
                .builder()
                .objectId(followeeId)
                .subjectId(followerId)
                .relationType(relationType.toString())
                .build();
    }
}
