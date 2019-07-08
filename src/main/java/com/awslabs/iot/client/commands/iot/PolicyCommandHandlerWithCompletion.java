package com.awslabs.iot.client.commands.iot;

import com.awslabs.iot.client.commands.iot.completers.PolicyCompleter;
import org.jline.reader.Completer;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.NullCompleter;

public interface PolicyCommandHandlerWithCompletion extends IotCommandHandler {
    @Override
    default Completer getCompleter() {
        return new ArgumentCompleter(getCommandNameCompleter(), getPolicyCompleter(), new NullCompleter());
    }

    PolicyCompleter getPolicyCompleter();
}
