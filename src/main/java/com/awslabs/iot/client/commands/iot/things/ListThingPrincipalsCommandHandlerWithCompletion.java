package com.awslabs.iot.client.commands.iot.things;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.iot.ThingCommandHandlerWithCompletion;
import com.awslabs.iot.client.commands.iot.completers.ThingCompleter;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.data.ImmutableThingName;
import com.awslabs.iot.data.ThingName;
import com.awslabs.iot.data.ThingPrincipal;
import com.awslabs.iot.helpers.interfaces.V2IotHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class ListThingPrincipalsCommandHandlerWithCompletion implements ThingCommandHandlerWithCompletion {
    private static final String LISTTHINGPRINCIPALS = "list-thing-principals";
    private static final int THING_NAME_POSITION = 0;
    private static final Logger log = LoggerFactory.getLogger(ListThingPrincipalsCommandHandlerWithCompletion.class);
    @Inject
    V2IotHelper v2IotHelper;
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

        ThingName thingName = ImmutableThingName.builder().name(parameters.get(THING_NAME_POSITION)).build();

        List<ThingPrincipal> principals = v2IotHelper.getThingPrincipals(thingName)
                .collect(Collectors.toList());

        if (principals.size() != 0) {
            log.info(String.join("", "Principals attached to thing [", thingName.getName(), "]"));
        }

        principals.forEach(principal -> log.info(String.join("", "  ", principal.getPrincipal())));
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
