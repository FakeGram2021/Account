package fakegram.container;


import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.server.EmbeddedServer;

public abstract class AbstractContainerBaseTest {

    protected static EmbeddedServer server;
    static TestCassandraContainer cassandraContainer;

    static {
        cassandraContainer = new TestCassandraContainer();
        cassandraContainer.start();
        server = ApplicationContext.run(EmbeddedServer.class);
    }
}
