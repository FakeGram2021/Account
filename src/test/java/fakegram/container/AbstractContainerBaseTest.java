package fakegram.container;


import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.server.EmbeddedServer;

public abstract class AbstractContainerBaseTest {

    protected static EmbeddedServer server;
    static TestCassandraContainer cassandraContainer;
    static TestKafkaContainer kafkaContainer;

    static {
        cassandraContainer = new TestCassandraContainer();
        kafkaContainer = new TestKafkaContainer();
        cassandraContainer.start();
        kafkaContainer.start();
        server = ApplicationContext.run(EmbeddedServer.class);
    }
}
