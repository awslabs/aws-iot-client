import com.awslabs.iot.client.applications.AwsIotClientModule;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {AwsIotClientModule.class})
public interface TestInjector {
}
