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
public class RelationBySubject {

    @PartitionKey
    private UUID subjectId;

    @ClusteringColumn(1)
    private String relationType;

    @ClusteringColumn(2)
    private UUID objectId;

    public static RelationBySubject from(UUID followerId, UUID followeeId, RelationType relationType) {
        return RelationBySubject.
                builder()
                .subjectId(followerId)
                .objectId(followeeId)
                .relationType(relationType.toString())
                .build();
    }
}
