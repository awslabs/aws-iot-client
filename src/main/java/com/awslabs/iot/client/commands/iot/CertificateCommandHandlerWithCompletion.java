package com.awslabs.iot.client.commands.iot;

import com.awslabs.iot.client.commands.iot.completers.CertificateCompleter;
import org.jline.reader.Completer;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.NullCompleter;

public interface CertificateCommandHandlerWithCompletion extends IotCommandHandler {
    @Override
    default Completer getCompleter() {
        return new ArgumentCompleter(getCommandNameCompleter(), getCertificateCompleter(), new NullCompleter());
    }

    CertificateCompleter getCertificateCompleter();
}
