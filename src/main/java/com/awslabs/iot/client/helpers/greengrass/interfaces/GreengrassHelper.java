package com.awslabs.iot.client.helpers.greengrass.interfaces;

import com.amazonaws.services.greengrass.model.*;

import java.util.List;
import java.util.Map;

public interface GreengrassHelper {
    List<GroupInformation> listGroups();

    List<String> listGroupArns();

    List<String> listGroupIds();

    Map<String, VersionInformation> listLatestGroupVersions();

    Map<String, VersionInformation> listLatestImmutableGroupVersions();

    List<DefinitionInformation> listNonImmutableCoreDefinitionInformation();

    List<DefinitionInformation> listNonImmutableDeviceDefinitionInformation();

    List<DefinitionInformation> listNonImmutableFunctionDefinitionInformation();

    List<DefinitionInformation> listNonImmutableLoggerDefinitionInformation();

    List<DefinitionInformation> listNonImmutableResourceDefinitionInformation();

    List<DefinitionInformation> listNonImmutableSubscriptionDefinitionInformation();

    List<VersionInformation> listGroupVersions(String groupId);

    VersionInformation getLatestGroupVersion(String groupId);

    List<Deployment> listDeployments(String groupId);

    List<String> listDeploymentIds(String groupId);

    Deployment getLatestDeployment(String groupId);

    String getDeploymentStatus(String groupId, String deploymentId);

    String getCoreDefinitionVersionArn(String groupId, VersionInformation versionInformation);

    GetCoreDefinitionResult getCoreDefinition(String groupId, VersionInformation versionInformation);

    GetGroupVersionResult getGroupVersion(String groupId, VersionInformation versionInformation);

    String getFunctionDefinitionVersionArn(String groupId, VersionInformation versionInformation);

    String getLoggerDefinitionVersionArn(String groupId, VersionInformation versionInformation);

    GetFunctionDefinitionResult getFunctionDefinition(String groupId, VersionInformation versionInformation);

    GetFunctionDefinitionVersionResult getFunctionDefinitionVersion(String groupId, VersionInformation versionInformation);

    String getResourceDefinitionVersionArn(String groupId, VersionInformation versionInformation);

    String getSubscriptionDefinitionVersionArn(String groupId, VersionInformation versionInformation);

    GetSubscriptionDefinitionResult getSubscriptionDefinition(String groupId, VersionInformation versionInformation);

    GetCoreDefinitionVersionResult getCoreDefinitionVersion(String groupId, VersionInformation versionInformation);

    GetSubscriptionDefinitionVersionResult getSubscriptionDefinitionVersion(String groupId, VersionInformation versionInformation);

    void deleteGroup(String groupId);

    GetDeviceDefinitionVersionResult getDeviceDefinitionVersion(String groupId, VersionInformation versionInformation);

    List<DefinitionInformation> listCoreDefinitions();

    void deleteCoreDefinition(DefinitionInformation definitionInformation);

    List<DefinitionInformation> listFunctionDefinitions();

    void deleteFunctionDefinition(DefinitionInformation definitionInformation);

    List<DefinitionInformation> listSubscriptionDefinitions();

    void deleteSubscriptionDefinition(DefinitionInformation definitionInformation);

    List<DefinitionInformation> listDeviceDefinitions();

    void deleteDeviceDefinition(DefinitionInformation definitionInformation);

    GetLoggerDefinitionVersionResult getLoggerDefinitionVersion(String groupId, VersionInformation versionInformation);

    void deleteLoggerDefinition(DefinitionInformation definitionInformation);

    List<DefinitionInformation> listLoggerDefinitions();

    GetResourceDefinitionVersionResult getResourceDefinitionVersion(String groupId, VersionInformation versionInformation);

    List<DefinitionInformation> listResourceDefinitions();

    void deleteResourceDefinition(DefinitionInformation definitionInformation);

    boolean groupExists(String groupId);

    boolean isGroupImmutable(String groupId);
}
