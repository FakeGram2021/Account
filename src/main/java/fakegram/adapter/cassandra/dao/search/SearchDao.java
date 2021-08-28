package fakegram.adapter.cassandra.dao.search;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Query;
import fakegram.adapter.cassandra.model.AccountInfo;

@Dao
public interface SearchDao {

    @Query("SELECT * FROM ${qualifiedTableId} WHERE username LIKE :username")
    PagingIterable<AccountInfo> searchByUsername(String username);
}
