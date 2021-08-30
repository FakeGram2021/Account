package fakegram;

import fakegram.integration.account.GetUserInfoTest;
import fakegram.integration.account.RegisterUserTest;
import fakegram.integration.account.UpdateUserTest;
import fakegram.integration.following.FollowingRequestTest;
import fakegram.integration.following.FollowingTest;
import fakegram.integration.following.GetFollowersTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        GetUserInfoTest.class,
        RegisterUserTest.class,
        UpdateUserTest.class,
        FollowingTest.class,
        FollowingRequestTest.class,
        GetFollowersTest.class
})
class AccountTest {
}
