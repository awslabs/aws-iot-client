package com.awslabs.iot.client.commands.iot;

import com.awslabs.iot.client.commands.iot.completers.ThingCompleter;
import org.jline.reader.Completer;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.NullCompleter;

public interface ThingCommandHandlerWithCompletion extends IotCommandHandler {
    @Override
    default Completer getCompleter() {
        return new ArgumentCompleter(getCommandNameCompleter(), getThingCompleter(), new NullCompleter());
    }

    ThingCompleter getThingCompleter();
}
