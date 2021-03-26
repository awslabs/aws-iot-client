package com.awslabs.iot.client.applications;

import com.awslabs.iot.client.commands.greengrassv2.greengrass.groups.DeleteAllCoresV2CommandHandler;
import com.awslabs.iot.client.commands.greengrassv2.greengrass.groups.ListCoresV2CommandHandler;
import com.awslabs.iot.client.commands.interfaces.CommandHandler;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ElementsIntoSet;
import io.vavr.collection.HashSet;

import java.util.Set;

@Module
public class GreengrassV2Module {
    @Provides
    @ElementsIntoSet
    public Set<CommandHandler> commandHandlerSet(ListCoresV2CommandHandler listCoresV2CommandHandler,
                                                 DeleteAllCoresV2CommandHandler deleteAllGroupsCommandHandler) {
        return HashSet.<CommandHandler>of(
                listCoresV2CommandHandler,
                deleteAllGroupsCommandHandler)
                .toJavaSet();
    }
}
