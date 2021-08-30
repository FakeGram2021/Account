package fakegram.container;

import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

public class TestCassandraContainer extends CassandraContainer<TestCassandraContainer> {

    private static final DockerImageName IMAGE_VERSION = DockerImageName.parse("djokicm/fakegram-cassandra:0.0.1").asCompatibleSubstituteFor("cassandra");

    public TestCassandraContainer() {
        super(IMAGE_VERSION);
    }

    @Override
    public void start() {
        this.withInitScript("cassandra.cql");
        this.withStartupTimeout(Duration.ofMinutes(15));
        super.start();
        System.setProperty("CASSANDRA_POINT", "localhost:" + this.getFirstMappedPort());
    }

    public void stop() {
        super.stop();
    }
}
