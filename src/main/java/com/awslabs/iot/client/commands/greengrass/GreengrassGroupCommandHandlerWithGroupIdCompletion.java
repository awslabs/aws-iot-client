package com.awslabs.iot.client.commands.greengrass;

import com.awslabs.iot.client.commands.greengrass.completers.GreengrassGroupIdCompleter;
import org.jline.reader.Completer;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.NullCompleter;

public interface GreengrassGroupCommandHandlerWithGroupIdCompletion extends GreengrassCommandHandler {
    @Override
    default Completer getCompleter() {
        return new ArgumentCompleter(getCommandNameCompleter(), getGreengrassGroupIdCompleter(), new NullCompleter());
    }

    GreengrassGroupIdCompleter getGreengrassGroupIdCompleter();
}
