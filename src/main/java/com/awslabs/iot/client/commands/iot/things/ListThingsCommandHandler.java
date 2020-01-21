package com.awslabs.iot.client.commands.iot.things;

import com.amazonaws.services.iot.model.ThingAttribute;
import com.awslabs.aws.iot.resultsiterator.helpers.interfaces.IoHelper;
import com.awslabs.aws.iot.resultsiterator.helpers.v1.interfaces.V1ThingHelper;
import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Optional;

public class ListThingsCommandHandler implements IotCommandHandler {
    private static final String LISTTHINGS = "list-things";
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ListThingsCommandHandler.class);
    @Inject
    Provider<V1ThingHelper> thingHelperProvider;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;

    @Inject
    public ListThingsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        thingHelperProvider.get().listThingAttributes()
                .forEach(this::logThingInfo);
    }

    private void logThingInfo(ThingAttribute thingAttribute) {
        Optional<String> optionalThingTypeName = Optional.ofNullable(thingAttribute.getThingTypeName());
        log.info("  [" + thingAttribute.getThingName() + "] [" + optionalThingTypeName.orElse("NO THING TYPE") + "]");
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
