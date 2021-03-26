package com.awslabs.iot.client.commands.iot.things;


import com.awslabs.iot.client.commands.iot.ThingCommandHandlerWithCompletion;
import com.awslabs.iot.client.commands.iot.completers.ThingCompleter;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.data.ImmutableThingName;
import com.awslabs.iot.data.ThingName;
import com.awslabs.iot.data.ThingPrincipal;
import com.awslabs.iot.helpers.interfaces.IotHelper;
import com.jcabi.log.Logger;
import io.vavr.collection.List;

import javax.inject.Inject;

public class ListThingPrincipalsCommandHandlerWithCompletion implements ThingCommandHandlerWithCompletion {
    private static final String LISTTHINGPRINCIPALS = "list-thing-principals";
    private static final int THING_NAME_POSITION = 0;
    @Inject
    IotHelper iotHelper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    ThingCompleter thingCompleter;

    @Inject
    public ListThingPrincipalsCommandHandlerWithCompletion() {
    }

    @Override
    public void innerHandle(String input) {
        List<String> parameters = parameterExtractor.getParameters(input);

        ThingName thingName = ImmutableThingName.builder().name(parameters.get(THING_NAME_POSITION)).build();

        List<ThingPrincipal> principals = iotHelper.getThingPrincipals(thingName)
                .toList();

        if (principals.size() != 0) {
            Logger.info(this, String.join("", "Principals attached to thing [", thingName.getName(), "]"));
        }

        principals.forEach(principal -> Logger.info(this, String.join("", "  ", principal.getPrincipal())));
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

    public ThingCompleter getThingCompleter() {
        return this.thingCompleter;
    }
}
