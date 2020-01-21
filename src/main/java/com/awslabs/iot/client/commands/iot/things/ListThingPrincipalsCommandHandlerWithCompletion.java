package com.awslabs.iot.client.commands.iot.things;

import com.amazonaws.services.iot.model.UnauthorizedException;
import com.awslabs.aws.iot.resultsiterator.helpers.interfaces.IoHelper;
import com.awslabs.aws.iot.resultsiterator.helpers.v1.interfaces.V1ThingHelper;
import com.awslabs.iot.client.commands.iot.ThingCommandHandlerWithCompletion;
import com.awslabs.iot.client.commands.iot.completers.ThingCompleter;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;

public class ListThingPrincipalsCommandHandlerWithCompletion implements ThingCommandHandlerWithCompletion {
    private static final String LISTTHINGPRINCIPALS = "list-thing-principals";
    private static final int THING_NAME_POSITION = 0;
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ListThingPrincipalsCommandHandlerWithCompletion.class);
    @Inject
    Provider<V1ThingHelper> thingHelperProvider;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;
    @Inject
    ThingCompleter thingCompleter;

    @Inject
    public ListThingPrincipalsCommandHandlerWithCompletion() {
    }

    @Override
    public void innerHandle(String input) {
        List<String> parameters = parameterExtractor.getParameters(input);

        String thingName = parameters.get(THING_NAME_POSITION);

        try {
            List<String> principals = thingHelperProvider.get().listPrincipals(thingName);

            if (principals.size() != 0) {
                log.info("Principals attached to thing [" + thingName + "]");
            }

            for (String principal : principals) {
                log.info("  " + principal);
            }
        } catch (UnauthorizedException e) {
            log.info("Couldn't list thing principals attached to this thing.  Either it doesn't exist or you do not have permissions to view it.");
        }
    }

    @Override
    public String getCommandString() {
        return LISTTHINGPRINCIPALS;
    }

    @Override
    public String getHelp() {
        return "Lists principals attached to a thing.";
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
