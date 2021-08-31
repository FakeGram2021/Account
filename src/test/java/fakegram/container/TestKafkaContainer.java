package fakegram.container;

import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

public class TestKafkaContainer extends KafkaContainer {

    private static final DockerImageName IMAGE = DockerImageName.parse("confluentinc/cp-kafka:6.2.0");

    public TestKafkaContainer() {
        super(IMAGE);
    }

    @Override
    public void start() {
        super.start();
        this.withStartupTimeout(Duration.ofMinutes(15));
        System.setProperty("KAFKA_SERVER", "localhost:" + this.getFirstMappedPort());
    }

    public void stop() {
        super.stop();
    }

}
