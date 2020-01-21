import com.awslabs.iot.client.applications.AwsIotClientConsole;
import com.awslabs.iot.client.commands.iot.mosh.BinaryMoshClientCommandHandler;
import com.awslabs.iot.client.commands.iot.mosh.MoshClientCommandHandler;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class MoshClientTest {
    private Injector injector;
    private MoshClientCommandHandler moshClientCommandHandler;

    @Before
    public void setup() {
        injector = AwsIotClientConsole.getInjector();

        // Prevents DefaultAwsRegionProviderChain failures in environments with no AWS configuration
        System.setProperty("aws.region", "us-east-1");

        moshClientCommandHandler = injector.getInstance(BinaryMoshClientCommandHandler.class);
    }

    @Test
    @Ignore
    public void shouldConnectToPi() {
        moshClientCommandHandler.innerHandle("BLAH pi_Core");
    }
}
