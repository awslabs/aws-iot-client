package com.awslabs.iot.client.applications;

import com.awslabs.iot.client.commands.interfaces.CommandHandler;
import com.awslabs.iot.client.commands.iot.certificates.*;
import com.awslabs.iot.client.commands.iot.mosh.BinaryMoshClientCommandHandler;
import com.awslabs.iot.client.commands.iot.mosh.MoshServerCommandHandler;
import com.awslabs.iot.client.commands.iot.mosh.MoshTopics;
import com.awslabs.iot.client.commands.iot.policies.DeletePolicyCommandHandlerWithCompletion;
import com.awslabs.iot.client.commands.iot.policies.ListPoliciesCommandHandler;
import com.awslabs.iot.client.commands.iot.publish.MqttPublishCommandHandler;
import com.awslabs.iot.client.commands.iot.publish.MqttSubscribeCommandHandler;
import com.awslabs.iot.client.commands.iot.publish.RestPublishCommandHandler;
import com.awslabs.iot.client.commands.iot.publish.TestPublishCommandHandler;
import com.awslabs.iot.client.commands.iot.rules.CreateTopicRuleCommandHandler;
import com.awslabs.iot.client.commands.iot.rules.DeleteTopicRuleCommandHandlerWithCompletion;
import com.awslabs.iot.client.commands.iot.rules.ListTopicRulesCommandHandler;
import com.awslabs.iot.client.commands.iot.things.*;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

class IotModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(MoshTopics.class).toInstance(new MoshTopics());

        // Vert.x
        // To get rid of "Failed to create cache dir" issue
        System.setProperty("vertx.disableFileCPResolving", "true");

        bind(Vertx.class).toInstance(Vertx.vertx(new VertxOptions()));

        Multibinder<CommandHandler> commandHandlerMultibinder = Multibinder.newSetBinder(binder(), CommandHandler.class);
        commandHandlerMultibinder.addBinding().to(TestPublishCommandHandler.class);
        commandHandlerMultibinder.addBinding().to(ListTopicRulesCommandHandler.class);
        commandHandlerMultibinder.addBinding().to(CreateTopicRuleCommandHandler.class);
        commandHandlerMultibinder.addBinding().to(DeleteTopicRuleCommandHandlerWithCompletion.class);
        commandHandlerMultibinder.addBinding().to(ListThingsCommandHandler.class);
        commandHandlerMultibinder.addBinding().to(ListThingPrincipalsCommandHandlerWithCompletion.class);
        commandHandlerMultibinder.addBinding().to(DeleteThingCommandHandlerWithCompletion.class);
        commandHandlerMultibinder.addBinding().to(DeleteAllThingsCommandHandler.class);
        commandHandlerMultibinder.addBinding().to(ListPoliciesCommandHandler.class);
        commandHandlerMultibinder.addBinding().to(DeletePolicyCommandHandlerWithCompletion.class);
        commandHandlerMultibinder.addBinding().to(CleanUpCertificatesCommandHandler.class);
        commandHandlerMultibinder.addBinding().to(DeleteCertificateCommandHandlerWithCompletion.class);
        commandHandlerMultibinder.addBinding().to(DeleteUnattachedCertificatesCommandHandlerWithCompletion.class);
        commandHandlerMultibinder.addBinding().to(DeleteAllCertificatesCommandHandler.class);
        commandHandlerMultibinder.addBinding().to(DeleteAllCaCertificatesCommandHandler.class);
        commandHandlerMultibinder.addBinding().to(ListCertificateArnsCommandHandler.class);
        commandHandlerMultibinder.addBinding().to(ListCertificateIdsCommandHandler.class);
        commandHandlerMultibinder.addBinding().to(RestPublishCommandHandler.class);
        commandHandlerMultibinder.addBinding().to(MqttPublishCommandHandler.class);
        commandHandlerMultibinder.addBinding().to(MqttSubscribeCommandHandler.class);
        commandHandlerMultibinder.addBinding().to(BinaryMoshClientCommandHandler.class);
        commandHandlerMultibinder.addBinding().to(MoshServerCommandHandler.class);
        commandHandlerMultibinder.addBinding().to(DeleteAllThingGroupsCommandHandler.class);
    }
}
