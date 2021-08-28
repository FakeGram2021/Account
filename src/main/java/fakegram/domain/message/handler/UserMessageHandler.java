package fakegram.domain.message.handler;

import fakegram.domain.model.account.User;

public interface UserMessageHandler {

    void sendUser(User user);
}
