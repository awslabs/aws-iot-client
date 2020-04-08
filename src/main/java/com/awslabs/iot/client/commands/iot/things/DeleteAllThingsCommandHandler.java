package com.awslabs.iot.client.commands.iot.things;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.data.ImmutableThingName;
import com.awslabs.iot.data.ThingName;
import com.awslabs.iot.helpers.interfaces.V2IotHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.iot.model.ThingAttribute;

import javax.inject.Inject;
import java.util.Comparator;

public class DeleteAllThingsCommandHandler implements IotCommandHandler {
    private static final String DELETEALLTHINGS = "delete-all-things";
    private static final Logger log = LoggerFactory.getLogger(DeleteAllThingsCommandHandler.class);
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;
    @Inject
    V2IotHelper v2IotHelper;

    @Inject
    public DeleteAllThingsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        v2IotHelper.getThings()
                // Sort the things by ID so we can get a general sense of how far along we are in the process of deleting them
                .sorted(Comparator.comparing(ThingAttribute::thingName))
                .forEach(this::attemptDeleteThing);
    }

    private void attemptDeleteThing(ThingAttribute thingAttribute) {
        ThingName thingName = ImmutableThingName.builder().name(thingAttribute.thingName()).build();

        if (v2IotHelper.isThingImmutable(thingName)) {
            log.info(String.join("", "Skipping thing [", thingName.getName(), "] because it is an immutable thing"));
            return;
        }

        log.info(String.join("", "Deleting thing [", thingName.getName(), "]"));

        // Rethrow all other exceptions
        v2IotHelper.delete(thingName);
    }

    @Override
    public String getCommandString() {
        return DELETEALLTHINGS;
    }

    @Override
    public String getHelp() {
        return "Deletes all things.";
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
