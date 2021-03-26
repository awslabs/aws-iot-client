package com.awslabs.iot.client.commands.greengrassv2.greengrass;

import com.awslabs.iot.client.commands.greengrassv2.greengrass.completers.GreengrassV2CoreNameCompleter;
import org.jline.reader.Completer;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.NullCompleter;

public interface GreengrassV2CommandHandlerWithCoreNameCompletion extends GreengrassV2CommandHandler {
    @Override
    default Completer getCompleter() {
        return new ArgumentCompleter(getCommandNameCompleter(), getGreengrassV2CoreNameCompleter(), new NullCompleter());
    }

    GreengrassV2CoreNameCompleter getGreengrassV2CoreNameCompleter();
}
