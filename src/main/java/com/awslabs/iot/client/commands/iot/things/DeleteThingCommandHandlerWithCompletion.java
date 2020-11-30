package com.awslabs.iot.client.commands.iot.things;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.iot.ThingCommandHandlerWithCompletion;
import com.awslabs.iot.client.commands.iot.completers.ThingCompleter;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.data.ImmutableThingName;
import com.awslabs.iot.data.ThingName;
import com.awslabs.iot.helpers.interfaces.V2IotHelper;

import javax.inject.Inject;
import java.util.List;

public class DeleteThingCommandHandlerWithCompletion implements ThingCommandHandlerWithCompletion {
    private static final String DELETETHING = "delete-thing";
    private static final int THING_NAME_POSITION = 0;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    V2IotHelper v2IotHelper;
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

        ThingName thingName = ImmutableThingName.builder().name(parameters.get(THING_NAME_POSITION)).build();

        v2IotHelper.delete(thingName);
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
