package com.awslabs.iot.client.commands.iot.things;

import com.awslabs.iot.client.commands.iot.ThingCommandHandlerWithCompletion;
import com.awslabs.iot.client.commands.iot.completers.ThingCompleter;
import com.awslabs.iot.client.helpers.io.interfaces.IOHelper;
import com.awslabs.iot.client.helpers.iot.exceptions.ThingAttachedToPrincipalsException;
import com.awslabs.iot.client.helpers.iot.interfaces.ThingHelper;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import io.vavr.control.Try;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;

public class DeleteThingCommandHandlerWithCompletion implements ThingCommandHandlerWithCompletion {
    private static final String DELETETHING = "delete-thing";
    private static final int THING_NAME_POSITION = 0;
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DeleteThingCommandHandlerWithCompletion.class);
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    Provider<ThingHelper> thingHelperProvider;
    @Inject
    IOHelper ioHelper;
    @Inject
    ThingCompleter thingCompleter;

    @Inject
    public DeleteThingCommandHandlerWithCompletion() {
    }

    @Override
    public void innerHandle(String input) {
        List<String> parameters = parameterExtractor.getParameters(input);

        String thingName = parameters.get(THING_NAME_POSITION);

        Try.of(() -> {
            thingHelperProvider.get().delete(thingName);
            return null;
        })
                .recover(ThingAttachedToPrincipalsException.class, throwable -> {
                    log.info("DELAY DISABLED!  GOOD LUCK WITH THAT!");

            /*
            write("Thing is attached to principals, cleaning up those principals in 5 seconds.  Press CTRL-C now to abort!");

            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e1) {
                throw new UnsupportedOperationException("Sleep was interrupted, quitting to avoid accidential deletion");
            }
            */

                    List<String> principalsDetached = thingHelperProvider.get().detachPrincipals(thingName);

                    for (String principal : principalsDetached) {
                        thingHelperProvider.get().deletePrincipal(principal);
                    }

                    // Try to delete it one last time
                    Try.of(() -> {
                        thingHelperProvider.get().delete(thingName);
                        return null;
                    })
                            .recover(ThingAttachedToPrincipalsException.class, secondThrowable -> {
                                log.info("Thing is still attached to principals, giving up");
                                return null;
                            })
                            .get();

                    return null;
                })
                .get();
    }

    @Override
    public String getCommandString() {
        return DELETETHING;
    }

    @Override
    public String getHelp() {
        return "Deletes a thing by name.";
    }

    @Override
    public int requiredParameters() {
        return 1;
    }

    public ParameterExtractor getParameterExtractor() {
        return this.parameterExtractor;
    }

    public IOHelper getIoHelper() {
        return this.ioHelper;
    }

    public ThingCompleter getThingCompleter() {
        return this.thingCompleter;
    }
}
