package fakegram.adapter.cassandra.mapper.account;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace;
import com.datastax.oss.driver.api.mapper.annotations.DaoTable;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;
import fakegram.adapter.cassandra.dao.account.AccountInfoDao;
import fakegram.adapter.cassandra.dao.search.SearchDao;

@Mapper
public interface AccountInfoMapper {

    @DaoFactory
    AccountInfoDao accountInfoDao(@DaoKeyspace CqlIdentifier keyspace, @DaoTable CqlIdentifier table);

    @DaoFactory
    SearchDao searchDao(@DaoKeyspace CqlIdentifier keyspace, @DaoTable CqlIdentifier table);
}
