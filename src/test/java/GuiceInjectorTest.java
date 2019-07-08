import com.awslabs.iot.client.applications.AwsIotClientConsole;
import com.awslabs.iot.client.interfaces.AwsIotClientTerminal;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;

import java.io.IOError;

public class GuiceInjectorTest {
    private Injector injector;

    @Before
    public void setup() {
        injector = AwsIotClientConsole.getInjector();

        // Prevents DefaultAwsRegionProviderChain failures in environments with no AWS configuration
        System.setProperty("aws.region", "us-east-1");
    }


    ///////////////////////////////////////////////////////////////////
    // These tests capture some injector related issues, but not all //
    ///////////////////////////////////////////////////////////////////

    @Test
    public void shouldGetAwsIotClientTerminalInstance() {
        injector.getInstance(AwsIotClientTerminal.class);
    }

    @Test(expected = IOError.class)
    public void shouldStartAwsIotClientTerminalInstance() throws Exception {
        System.in.close();
        injector.getInstance(AwsIotClientTerminal.class).start();
    }
}
