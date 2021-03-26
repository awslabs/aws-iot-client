package com.awslabs.iot.client.commands.iot.things;


import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.helpers.progressbar.ProgressBarHelper;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.client.streams.interfaces.UsesStream;
import com.awslabs.iot.data.ImmutableThingName;
import com.awslabs.iot.helpers.interfaces.IotHelper;
import io.vavr.collection.Stream;
import io.vavr.control.Try;
import software.amazon.awssdk.services.iot.model.ThingAttribute;

import javax.inject.Inject;

public class DeleteAllThingsCommandHandler implements IotCommandHandler, UsesStream<ThingAttribute> {
    private static final String DELETEALLTHINGS = "delete-all-things";
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IotHelper iotHelper;
    @Inject
    ProgressBarHelper progressBarHelper;

    @Inject
    public DeleteAllThingsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        Try.withResources(() -> progressBarHelper.start("Delete all IoT things", this))
                .of(progressBar -> run());
    }

    private Void run() {
        getStream()
                .map(thingAttribute -> ImmutableThingName.builder().name(thingAttribute.thingName()).build())
                // Filter out non-immutable things
                .filter(thingName -> !iotHelper.isThingImmutable(thingName))
                .peek(thingName -> progressBarHelper.next())
                // Rethrow all exceptions
                .forEach(iotHelper::delete);

        return null;
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

    @Override
    public Stream<ThingAttribute> getStream() {
        return iotHelper.getThings();
    }
}
