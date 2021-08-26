package fakegram.container;


public abstract class AbstractContainerBaseTest {

    static TestCassandraContainer cassandraContainer;

    static {
        cassandraContainer = new TestCassandraContainer();
        cassandraContainer.start();
    }

}
