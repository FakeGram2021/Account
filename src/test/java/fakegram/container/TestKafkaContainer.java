package fakegram.container;

import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

public class TestKafkaContainer extends KafkaContainer {

    private static DockerImageName IMAGE = DockerImageName.parse("confluentinc/cp-kafka:6.2.0");

    public TestKafkaContainer() {
        super(IMAGE);
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("KAFKA_SERVER", "localhost:" + this.getFirstMappedPort());
    }

    public void stop() {
        super.stop();
    }

}
