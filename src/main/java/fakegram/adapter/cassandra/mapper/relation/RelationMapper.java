package fakegram.adapter.cassandra.mapper.relation;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace;
import com.datastax.oss.driver.api.mapper.annotations.DaoTable;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;
import fakegram.adapter.cassandra.dao.relation.RelationByObjectDao;
import fakegram.adapter.cassandra.dao.relation.RelationBySubjectDao;

@Mapper
public interface RelationMapper {

    @DaoFactory
    RelationByObjectDao relationByObjectDao(@DaoKeyspace CqlIdentifier keyspace, @DaoTable CqlIdentifier table);

    @DaoFactory
    RelationBySubjectDao relationBySubjectDao(@DaoKeyspace CqlIdentifier keyspace, @DaoTable CqlIdentifier table);
}
