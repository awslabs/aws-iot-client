package com.awslabs.iot.client.commands.iot.things;


import com.awslabs.iot.client.commands.iot.ThingCommandHandlerWithCompletion;
import com.awslabs.iot.client.commands.iot.completers.ThingCompleter;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.data.ImmutableThingName;
import com.awslabs.iot.data.ThingName;
import com.awslabs.iot.helpers.interfaces.IotHelper;
import io.vavr.collection.List;

import javax.inject.Inject;

public class DeleteThingCommandHandlerWithCompletion implements ThingCommandHandlerWithCompletion {
    private static final String DELETETHING = "delete-thing";
    private static final int THING_NAME_POSITION = 0;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IotHelper iotHelper;
    @Inject
    ThingCompleter thingCompleter;

    @Inject
    public DeleteThingCommandHandlerWithCompletion() {
    }

    @Override
    public void innerHandle(String input) {
        List<String> parameters = parameterExtractor.getParameters(input);

        ThingName thingName = ImmutableThingName.builder().name(parameters.get(THING_NAME_POSITION)).build();

        iotHelper.delete(thingName);
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

    public ThingCompleter getThingCompleter() {
        return this.thingCompleter;
    }
}
