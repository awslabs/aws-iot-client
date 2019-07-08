package com.awslabs.iot.client.commands.iot;

import com.awslabs.iot.client.commands.iot.completers.RuleCompleter;
import org.jline.reader.Completer;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.NullCompleter;

public interface RuleCommandHandlerWithCompletion extends IotCommandHandler {
    @Override
    default Completer getCompleter() {
        return new ArgumentCompleter(getCommandNameCompleter(), getRuleCompleter(), new NullCompleter());
    }

    RuleCompleter getRuleCompleter();
}
