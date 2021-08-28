package fakegram.adapter.cassandra.dao.account;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import com.datastax.oss.driver.api.mapper.annotations.Query;
import fakegram.adapter.cassandra.model.AccountInfo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Dao
public interface AccountInfoDao {

    @Query("SELECT * FROM ${qualifiedTableId} WHERE account_id in :accountIds")
    PagingIterable<AccountInfo> findByAccountIds(List<UUID> accountIds);

    @Query("SELECT * FROM ${qualifiedTableId} WHERE account_id = :accountId")
    Optional<AccountInfo> findByAccountId(UUID accountId);

    @Query("Select * FROM ${qualifiedTableId} WHERE username = :username")
    Optional<AccountInfo> findByUsername(String username);

    @Insert
    void upsert(AccountInfo accountInfo);
}
