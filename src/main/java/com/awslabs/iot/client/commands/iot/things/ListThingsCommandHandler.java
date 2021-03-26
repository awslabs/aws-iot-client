package com.awslabs.iot.client.commands.iot.things;


import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.helpers.interfaces.IotHelper;
import com.jcabi.log.Logger;
import io.vavr.control.Option;
import software.amazon.awssdk.services.iot.model.ThingAttribute;

import javax.inject.Inject;

public class ListThingsCommandHandler implements IotCommandHandler {
    private static final String LISTTHINGS = "list-things";
    @Inject
    IotHelper iotHelper;
    @Inject
    ParameterExtractor parameterExtractor;

    @Inject
    public ListThingsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        iotHelper.getThings()
                .forEach(this::logThingInfo);
    }

    private void logThingInfo(ThingAttribute thingAttribute) {
        Option<String> optionalThingTypeName = Option.of(thingAttribute.thingTypeName());
        Logger.info(this, String.join("", "  [", thingAttribute.thingName(), "] [", optionalThingTypeName.getOrElse("NO THING TYPE"), "]"));
    }

    @Override
    public String getCommandString() {
        return LISTTHINGS;
    }

    @Override
    public String getHelp() {
        return "Lists all things.";
    }

    @Override
    public int requiredParameters() {
        return 0;
    }

    public ParameterExtractor getParameterExtractor() {
        return this.parameterExtractor;
    }
}
