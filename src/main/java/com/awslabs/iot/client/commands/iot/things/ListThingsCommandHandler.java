package com.awslabs.iot.client.commands.iot.things;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.helpers.interfaces.V2IotHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.iot.model.ThingAttribute;

import javax.inject.Inject;
import java.util.Optional;

public class ListThingsCommandHandler implements IotCommandHandler {
    private static final String LISTTHINGS = "list-things";
    private static final Logger log = LoggerFactory.getLogger(ListThingsCommandHandler.class);
    @Inject
    V2IotHelper v2IotHelper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;

    @Inject
    public ListThingsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        v2IotHelper.getThings()
                .forEach(this::logThingInfo);
    }

    private void logThingInfo(ThingAttribute thingAttribute) {
        Optional<String> optionalThingTypeName = Optional.ofNullable(thingAttribute.thingTypeName());
        log.info(String.join("", "  [", thingAttribute.thingName(), "] [", optionalThingTypeName.orElse("NO THING TYPE"), "]"));
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

    public IoHelper getIoHelper() {
        return this.ioHelper;
    }
}
