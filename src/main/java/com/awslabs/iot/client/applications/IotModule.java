package com.awslabs.iot.client.applications;

import com.awslabs.aws.iot.websockets.BasicMqttOverWebsocketsProvider;
import com.awslabs.aws.iot.websockets.MqttOverWebsocketsProvider;
import com.awslabs.iot.client.commands.interfaces.CommandHandler;
import com.awslabs.iot.client.commands.iot.certificates.*;
import com.awslabs.iot.client.commands.iot.jobs.DeleteAllJobsCommandHandler;
import com.awslabs.iot.client.commands.iot.policies.DeleteAllPoliciesCommandHandler;
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
import com.awslabs.iot.client.helpers.iot.BasicWebsocketsHelper;
import com.awslabs.iot.client.helpers.iot.interfaces.WebsocketsHelper;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ElementsIntoSet;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

import javax.inject.Singleton;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Module
public class IotModule {
    @Provides
    public WebsocketsHelper websocketsHelper(BasicWebsocketsHelper basicWebsocketsHelper) {
        return basicWebsocketsHelper;
    }

    @Provides
    public MqttOverWebsocketsProvider mqttOverWebsocketsProvider() {
        return new BasicMqttOverWebsocketsProvider();
    }

    @Provides
    @Singleton
    public Vertx vertx() {
        // To get rid of "Failed to create cache dir" issue
        System.setProperty("vertx.disableFileCPResolving", "true");

        return Vertx.vertx(new VertxOptions());
    }

    @Provides
    @ElementsIntoSet
    public Set<CommandHandler> commandHandlerSet(TestPublishCommandHandler testPublishCommandHandler,
                                                 ListTopicRulesCommandHandler listTopicRulesCommandHandler,
                                                 CreateTopicRuleCommandHandler createTopicRuleCommandHandler,
                                                 DeleteTopicRuleCommandHandlerWithCompletion deleteTopicRuleCommandHandlerWithCompletion,
                                                 ListThingsCommandHandler listThingsCommandHandler,
                                                 ListThingPrincipalsCommandHandlerWithCompletion listThingPrincipalsCommandHandlerWithCompletion,
                                                 DeleteThingCommandHandlerWithCompletion deleteThingCommandHandlerWithCompletion,
                                                 DeleteAllThingsCommandHandler deleteAllThingsCommandHandler,
                                                 ListPoliciesCommandHandler listPoliciesCommandHandler,
                                                 DeletePolicyCommandHandlerWithCompletion deletePolicyCommandHandlerWithCompletion,
                                                 CleanUpCertificatesCommandHandler cleanUpCertificatesCommandHandler,
                                                 DeleteCertificateCommandHandlerWithCompletion deleteCertificateCommandHandlerWithCompletion,
                                                 DeleteUnattachedCertificatesCommandHandlerWithCompletion deleteUnattachedCertificatesCommandHandlerWithCompletion,
                                                 DeleteAllCertificatesCommandHandler deleteAllCertificatesCommandHandler,
                                                 DeleteAllPoliciesCommandHandler deleteAllPoliciesCommandHandler,
                                                 DeleteAllCaCertificatesCommandHandler deleteAllCaCertificatesCommandHandler,
                                                 ListCertificateArnsCommandHandler listCertificateArnsCommandHandler,
                                                 ListCertificateIdsCommandHandler listCertificateIdsCommandHandler,
                                                 RestPublishCommandHandler restPublishCommandHandler,
                                                 MqttPublishCommandHandler mqttPublishCommandHandler,
                                                 MqttSubscribeCommandHandler mqttSubscribeCommandHandler,
                                                 DeleteAllThingGroupsCommandHandler deleteAllThingGroupsCommandHandler,
                                                 DeleteAllJobsCommandHandler deleteAllJobsCommandHandler) {
        return new HashSet<>(Arrays.asList(
                testPublishCommandHandler,
                listTopicRulesCommandHandler,
                createTopicRuleCommandHandler,
                deleteTopicRuleCommandHandlerWithCompletion,
                listThingsCommandHandler,
                listThingPrincipalsCommandHandlerWithCompletion,
                deleteThingCommandHandlerWithCompletion,
                deleteAllThingsCommandHandler,
                listPoliciesCommandHandler,
                deletePolicyCommandHandlerWithCompletion,
                cleanUpCertificatesCommandHandler,
                deleteCertificateCommandHandlerWithCompletion,
                deleteUnattachedCertificatesCommandHandlerWithCompletion,
                deleteAllCertificatesCommandHandler,
                deleteAllPoliciesCommandHandler,
                deleteAllCaCertificatesCommandHandler,
                listCertificateArnsCommandHandler,
                listCertificateIdsCommandHandler,
                restPublishCommandHandler,
                mqttPublishCommandHandler,
                mqttSubscribeCommandHandler,
                deleteAllThingGroupsCommandHandler,
                deleteAllJobsCommandHandler));
    }
}
