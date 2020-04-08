package com.awslabs.iot.client.commands.iot.things;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.iot.ThingCommandHandlerWithCompletion;
import com.awslabs.iot.client.commands.iot.completers.ThingCompleter;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.exceptions.ThingAttachedToPrincipalsException;
import com.awslabs.iot.helpers.interfaces.V1ThingHelper;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;

public class DeleteThingCommandHandlerWithCompletion implements ThingCommandHandlerWithCompletion {
    private static final String DELETETHING = "delete-thing";
    private static final int THING_NAME_POSITION = 0;
    private static final Logger log = LoggerFactory.getLogger(DeleteThingCommandHandlerWithCompletion.class);
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    Provider<V1ThingHelper> thingHelperProvider;
    @Inject
    IoHelper ioHelper;
    @Inject
    ThingCompleter thingCompleter;

    @Inject
    public DeleteThingCommandHandlerWithCompletion() {
    }

    @Override
    public void innerHandle(String input) {
        List<String> parameters = parameterExtractor.getParameters(input);

        String thingName = parameters.get(THING_NAME_POSITION);

        Try.run(() -> thingHelperProvider.get().delete(thingName))
                .recover(ThingAttachedToPrincipalsException.class, exception -> forceDelete(exception, thingName))
                // Rethrow all other exceptions
                .get();
    }

    private Void forceDelete(ThingAttachedToPrincipalsException thingAttachedToPrincipalsException, String thingName) {
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
        Try.run(() -> thingHelperProvider.get().delete(thingName))
                .recover(ThingAttachedToPrincipalsException.class, this::logAndSkipDelete)
                // Rethrow all other exceptions
                .get();

        return null;
    }

    private Void logAndSkipDelete(ThingAttachedToPrincipalsException thingAttachedToPrincipalsException) {
        log.info("Thing is still attached to principals, skipping");

        return null;
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

    public IoHelper getIoHelper() {
        return this.ioHelper;
    }

    public ThingCompleter getThingCompleter() {
        return this.thingCompleter;
    }
}
