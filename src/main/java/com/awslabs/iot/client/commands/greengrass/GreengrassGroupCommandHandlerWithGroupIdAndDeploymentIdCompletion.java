package com.awslabs.iot.client.commands.greengrass;

import com.awslabs.iot.client.commands.greengrass.completers.GreengrassGroupIdAndDeploymentIdCompleter;
import org.jline.reader.Completer;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.NullCompleter;

public interface GreengrassGroupCommandHandlerWithGroupIdAndDeploymentIdCompletion extends GreengrassCommandHandler {
    @Override
    default Completer getCompleter() {
        return new ArgumentCompleter(getCommandNameCompleter(), getGreengrassGroupIdAndDeploymentIdCompleter(), getGreengrassGroupIdAndDeploymentIdCompleter(), new NullCompleter());
    }

    GreengrassGroupIdAndDeploymentIdCompleter getGreengrassGroupIdAndDeploymentIdCompleter();
}
