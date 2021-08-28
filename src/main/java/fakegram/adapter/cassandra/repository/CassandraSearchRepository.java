package fakegram.adapter.cassandra.repository;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import fakegram.adapter.cassandra.dao.search.SearchDao;
import fakegram.adapter.cassandra.mapper.account.AccountInfoMapper;
import fakegram.adapter.cassandra.mapper.account.AccountInfoMapperBuilder;
import fakegram.adapter.cassandra.model.AccountInfo;
import fakegram.domain.model.account.User;
import fakegram.domain.repository.SearchRepository;

import javax.inject.Inject;
import java.util.Collection;

public class CassandraSearchRepository implements SearchRepository {

    private static final String ACCOUNT_INFO_ACCOUNT_ID_TABLE_NAME = "account_info_by_account_id";

    private final CqlSession session;
    private final SearchDao searchDao;

    @Inject
    public CassandraSearchRepository(CqlSession session) {
        this.session = session;
        AccountInfoMapper accountInfoMapper = new AccountInfoMapperBuilder(session).build();
        searchDao = accountInfoMapper.searchDao(
                CqlIdentifier.fromCql("account"),
                CqlIdentifier.fromCql(ACCOUNT_INFO_ACCOUNT_ID_TABLE_NAME)
        );
    }

    @Override
    public Collection<User> searchByUsername(String username) {
        String searchTerm = username + "%";
        return searchDao.searchByUsername(searchTerm).map(AccountInfo::toUser).all();
    }
}
