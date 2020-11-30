package com.awslabs.iot.client.commands.iot.things;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.helpers.progressbar.ProgressBarHelper;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.client.streams.interfaces.UsesStream;
import com.awslabs.iot.data.ImmutableThingName;
import com.awslabs.iot.helpers.interfaces.V2IotHelper;
import io.vavr.control.Try;
import software.amazon.awssdk.services.iot.model.ThingAttribute;

import javax.inject.Inject;
import java.util.stream.Stream;

public class DeleteAllThingsCommandHandler implements IotCommandHandler, UsesStream<ThingAttribute> {
    private static final String DELETEALLTHINGS = "delete-all-things";
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;
    @Inject
    V2IotHelper v2IotHelper;
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
                .filter(thingName -> !v2IotHelper.isThingImmutable(thingName))
                .peek(thingName -> progressBarHelper.next())
                // Rethrow all exceptions
                .forEach(v2IotHelper::delete);

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

    public IoHelper getIoHelper() {
        return this.ioHelper;
    }

    @Override
    public Stream<ThingAttribute> getStream() {
        return v2IotHelper.getThings();
    }
}
