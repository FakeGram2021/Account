package fakegram.adapter.cassandra.repository;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import fakegram.adapter.cassandra.dao.account.AccountInfoDao;
import fakegram.adapter.cassandra.mapper.account.AccountInfoMapper;
import fakegram.adapter.cassandra.mapper.account.AccountInfoMapperBuilder;
import fakegram.adapter.cassandra.model.AccountInfo;
import fakegram.domain.model.account.User;
import fakegram.domain.repository.UserRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static fakegram.adapter.cassandra.model.AccountInfo.from;

@Singleton
public class CassandraUserRepository implements UserRepository {

    private static final String ACCOUNT_INFO_ACCOUNT_ID_TABLE_NAME = "account_info_by_account_id";

    private final AccountInfoDao accountInfoDao;

    @Inject
    public CassandraUserRepository(final CqlSession session) {
        final AccountInfoMapper accountInfoMapper = new AccountInfoMapperBuilder(session).build();
        accountInfoDao = accountInfoMapper.accountInfoDao(
                CqlIdentifier.fromCql("account"),
                CqlIdentifier.fromCql(ACCOUNT_INFO_ACCOUNT_ID_TABLE_NAME)
        );
    }

    @Override
    public void registerUser(User account) {
        AccountInfo accountInfo = from(account);
        accountInfoDao.upsert(accountInfo);
    }

    @Override
    public User findByAccountId(UUID accountId) {
        return accountInfoDao.findByAccountId(accountId).get().toUser();
    }

    @Override
    public User findByUsername(String username) {
        Optional<AccountInfo> result = accountInfoDao.findByUsername(username);
        return result.map(AccountInfo::toUser).orElse(null);
    }

    @Override
    public List<User> findByAccountIds(Collection<UUID> accountIds) {
        List<UUID> a = (List<UUID>) accountIds;
        return accountInfoDao.findByAccountIds(a)
                .all()
                .stream()
                .map(AccountInfo::toUser)
                .collect(Collectors.toList());
    }

    @Override
    public void updateUser(User account) {
        AccountInfo accountInfo = from(account);
        accountInfoDao.upsert(accountInfo);
    }
}
