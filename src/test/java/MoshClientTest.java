import com.awslabs.iot.client.commands.iot.mosh.MoshClientCommandHandler;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class MoshClientTest {
    private MoshClientCommandHandler moshClientCommandHandler;

    @Before
    public void setup() {
        TestInjector testInjector = DaggerTestInjector.create();

        // Prevents DefaultAwsRegionProviderChain failures in environments with no AWS configuration
        System.setProperty("aws.region", "us-east-1");

        throw new RuntimeException("Not implemented yet");
//        moshClientCommandHandler = testInjector.moshClientCommandHandler();
    }

    @Test
    @Ignore
    public void shouldConnectToPi() {
        moshClientCommandHandler.innerHandle("BLAH pi_Core");
    }
}
