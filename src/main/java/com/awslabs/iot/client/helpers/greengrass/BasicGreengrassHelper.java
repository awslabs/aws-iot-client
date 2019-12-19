package com.awslabs.iot.client.helpers.greengrass;

import com.amazonaws.services.greengrass.AWSGreengrassClient;
import com.amazonaws.services.greengrass.model.*;
import com.awslabs.aws.iot.resultsiterator.ResultsIterator;
import com.awslabs.iot.client.helpers.greengrass.interfaces.GreengrassHelper;
import com.awslabs.iot.client.helpers.greengrass.interfaces.IdExtractor;
import com.awslabs.iot.client.helpers.iot.interfaces.ThingHelper;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BasicGreengrassHelper implements GreengrassHelper {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(BasicGreengrassHelper.class);
    @Inject
    AWSGreengrassClient awsGreengrassClient;
    @Inject
    IdExtractor idExtractor;
    @Inject
    Provider<ThingHelper> thingHelperProvider;

    @Inject
    public BasicGreengrassHelper() {
    }

    @Override
    public List<GroupInformation> listGroups() {
        List<GroupInformation> groupInformationList = new ResultsIterator<GroupInformation>(awsGreengrassClient, ListGroupsRequest.class, ListGroupsResult.class).iterateOverResults();

        // Return the list sorted so overlapping names can be found easily
        sortGroupInformation(groupInformationList);

        return groupInformationList;
    }

    @Override
    public List<String> listGroupArns() {
        return (List<String>) mapGroupInfo(GroupInformation::getArn);
    }

    @Override
    public List<String> listGroupIds() {
        return (List<String>) mapGroupInfo(GroupInformation::getId);
    }

    @Override
    public Map<String, VersionInformation> listLatestGroupVersions() {
        List<GroupInformation> allGroupInformation = listAllGroupInformation();

        Map<String, VersionInformation> latestGroupInformation = allGroupInformation.stream()
                .collect(Collectors.toMap(group -> group.getId(), group -> getLatestGroupVersion(group.getId())));

        return latestGroupInformation;
    }

    @Override
    public Map<String, VersionInformation> listLatestImmutableGroupVersions() {
        List<GroupInformation> allGroupInformation = listAllGroupInformation();

        Map<String, VersionInformation> latestGroupInformation = allGroupInformation.stream()
                .filter(group -> isGroupImmutable(group.getId()))
                .collect(Collectors.toMap(group -> group.getId(), group -> getLatestGroupVersion(group.getId())));

        return latestGroupInformation;
    }

    @Override
    public List<DefinitionInformation> listNonImmutableCoreDefinitionInformation() {
        return listNonImmutableDefinitionInformation(this::listCoreDefinitions, this::listLatestImmutableCoreDefinitionVersionArns);
    }

    @Override
    public List<DefinitionInformation> listNonImmutableDeviceDefinitionInformation() {
        return listNonImmutableDefinitionInformation(this::listDeviceDefinitions, this::listLatestImmutableDeviceDefinitionVersionArns);
    }

    @Override
    public List<DefinitionInformation> listNonImmutableFunctionDefinitionInformation() {
        return listNonImmutableDefinitionInformation(this::listFunctionDefinitions, this::listLatestImmutableFunctionDefinitionVersionArns);
    }

    @Override
    public List<DefinitionInformation> listNonImmutableLoggerDefinitionInformation() {
        return listNonImmutableDefinitionInformation(this::listLoggerDefinitions, this::listLatestImmutableLoggerDefinitionVersionArns);
    }

    @Override
    public List<DefinitionInformation> listNonImmutableResourceDefinitionInformation() {
        return listNonImmutableDefinitionInformation(this::listResourceDefinitions, this::listLatestImmutableResourceDefinitionVersionArns);
    }

    @Override
    public List<DefinitionInformation> listNonImmutableSubscriptionDefinitionInformation() {
        return listNonImmutableDefinitionInformation(this::listSubscriptionDefinitions, this::listLatestImmutableSubscriptionDefinitionVersionArns);
    }

    private List<DefinitionInformation> listNonImmutableDefinitionInformation(Supplier<List<DefinitionInformation>> definitionInformationSupplier,
                                                                              Supplier<Set<String>> immutableDefinitionVersionArnSupplier) {
        List<DefinitionInformation> definitionInformationList = definitionInformationSupplier.get();
        Set<String> latestCoreDefinitions = immutableDefinitionVersionArnSupplier.get();

        // Remove all definitions that are immutable versions
        return definitionInformationList = definitionInformationList.stream()
                .filter(d -> !latestCoreDefinitions.contains(d.getLatestVersionArn()))
                .collect(Collectors.toList());
    }

    private Set<String> listLatestImmutableCoreDefinitionVersionArns() {
        return listLatestImmutableGroupVersions().entrySet().stream()
                .map(e -> getCoreDefinitionVersion(e.getKey(), e.getValue()))
                .filter(Objects::nonNull)
                .map(v -> v.getArn())
                .collect(Collectors.toSet());
    }

    private Set<String> listLatestImmutableDeviceDefinitionVersionArns() {
        return listLatestImmutableGroupVersions().entrySet().stream()
                .map(e -> getDeviceDefinitionVersion(e.getKey(), e.getValue()))
                .filter(Objects::nonNull)
                .map(v -> v.getArn())
                .collect(Collectors.toSet());
    }

    private Set<String> listLatestImmutableFunctionDefinitionVersionArns() {
        return listLatestImmutableGroupVersions().entrySet().stream()
                .map(e -> getFunctionDefinitionVersion(e.getKey(), e.getValue()))
                .filter(Objects::nonNull)
                .map(v -> v.getArn())
                .collect(Collectors.toSet());
    }

    private Set<String> listLatestImmutableLoggerDefinitionVersionArns() {
        return listLatestImmutableGroupVersions().entrySet().stream()
                .map(e -> getLoggerDefinitionVersion(e.getKey(), e.getValue()))
                .filter(Objects::nonNull)
                .map(v -> v.getArn())
                .collect(Collectors.toSet());
    }

    private Set<String> listLatestImmutableResourceDefinitionVersionArns() {
        return listLatestImmutableGroupVersions().entrySet().stream()
                .map(e -> getResourceDefinitionVersion(e.getKey(), e.getValue()))
                .filter(Objects::nonNull)
                .map(v -> v.getArn())
                .collect(Collectors.toSet());
    }

    private Set<String> listLatestImmutableSubscriptionDefinitionVersionArns() {
        return listLatestImmutableGroupVersions().entrySet().stream()
                .map(e -> getSubscriptionDefinitionVersion(e.getKey(), e.getValue()))
                .filter(Objects::nonNull)
                .map(v -> v.getArn())
                .collect(Collectors.toSet());
    }

    private List<?> mapGroupInfo(Function<? super GroupInformation, ?> x) {
        List<GroupInformation> groupInformationList = listGroups();

        return groupInformationList.stream()
                .map(x)
                .collect(Collectors.toList());
    }

    private List<GroupInformation> listAllGroupInformation() {
        List<GroupInformation> groupInformationList = listGroups();

        return groupInformationList.stream()
                .collect(Collectors.toList());
    }

    @Override
    public List<VersionInformation> listGroupVersions(String groupId) {
        ListGroupVersionsRequest listGroupVersionsRequest = new ListGroupVersionsRequest().withGroupId(groupId);

        List<VersionInformation> versionInformationList = new ResultsIterator<VersionInformation>(awsGreengrassClient, listGroupVersionsRequest, ListGroupVersionsResult.class).iterateOverResults();

        // Return the list sorted so we can easily find the latest version
        sortGroupVersionInformation(versionInformationList);

        return versionInformationList;
    }

    @Override
    public VersionInformation getLatestGroupVersion(String groupId) {
        List<VersionInformation> versionInformationList = listGroupVersions(groupId);

        if (versionInformationList.size() == 0) {
            return null;
        }

        return versionInformationList.get(versionInformationList.size() - 1);
    }

    @Override
    public List<Deployment> listDeployments(String groupId) {
        ListDeploymentsRequest listDeploymentsRequest = new ListDeploymentsRequest()
                .withGroupId(groupId);

        List<Deployment> deploymentsList = new ResultsIterator<Deployment>(awsGreengrassClient, listDeploymentsRequest, ListDeploymentsResult.class).iterateOverResults();

        // Return the list sorted so we can easily find the latest deployment
        sortDeployments(deploymentsList);

        return deploymentsList;
    }

    @Override
    public List<String> listDeploymentIds(String groupId) {
        return listDeployments(groupId)
                .stream()
                .map(Deployment::getDeploymentId)
                .collect(Collectors.toList());
    }

    @Override
    public Deployment getLatestDeployment(String groupId) {
        List<Deployment> deploymentList = listDeployments(groupId);

        if (deploymentList.size() == 0) {
            return null;
        }

        return deploymentList.get(deploymentList.size() - 1);
    }

    @Override
    public String getDeploymentStatus(String groupId, String deploymentId) {
        GetDeploymentStatusRequest getDeploymentStatusRequest = new GetDeploymentStatusRequest()
                .withGroupId(groupId)
                .withDeploymentId(deploymentId);

        GetDeploymentStatusResult getDeploymentStatusResult = awsGreengrassClient.getDeploymentStatus(getDeploymentStatusRequest);

        if (getDeploymentStatusResult == null) {
            return null;
        }

        return getDeploymentStatusResult.getDeploymentStatus();
    }

    @Override
    public String getCoreDefinitionVersionArn(String groupId, VersionInformation versionInformation) {
        GetGroupVersionResult getGroupVersionResult = getGroupVersion(groupId, versionInformation);
        GroupVersion groupVersion = getGroupVersionResult.getDefinition();

        return groupVersion.getCoreDefinitionVersionArn();
    }

    @Override
    public String getConnectorDefinitionVersionArn(String groupId, VersionInformation versionInformation) {
        GetGroupVersionResult getGroupVersionResult = getGroupVersion(groupId, versionInformation);
        GroupVersion groupVersion = getGroupVersionResult.getDefinition();

        return groupVersion.getConnectorDefinitionVersionArn();
    }

    @Override
    public GetCoreDefinitionResult getCoreDefinition(String groupId, VersionInformation versionInformation) {
        String coreDefinitionVersionArn = getCoreDefinitionVersionArn(groupId, versionInformation);

        GetCoreDefinitionRequest getCoreDefinitionRequest = new GetCoreDefinitionRequest()
                .withCoreDefinitionId(idExtractor.extractId(coreDefinitionVersionArn));
        return awsGreengrassClient.getCoreDefinition(getCoreDefinitionRequest);
    }

    @Override
    public GetCoreDefinitionVersionResult getCoreDefinitionVersion(String groupId, VersionInformation versionInformation) {
        String coreDefinitionVersionArn = getCoreDefinitionVersionArn(groupId, versionInformation);

        return getCoreDefinitionVersion(idExtractor.extractId(coreDefinitionVersionArn), idExtractor.extractVersionId(coreDefinitionVersionArn));
    }

    private GetCoreDefinitionVersionResult getCoreDefinitionVersion(String coreDefinitionId, String coreDefinitionVersionId) {
        GetCoreDefinitionVersionRequest getCoreDefinitionVersionRequest = new GetCoreDefinitionVersionRequest()
                .withCoreDefinitionId(coreDefinitionId)
                .withCoreDefinitionVersionId(coreDefinitionVersionId);
        return awsGreengrassClient.getCoreDefinitionVersion(getCoreDefinitionVersionRequest);
    }

    @Override
    public GetGroupVersionResult getGroupVersion(String groupId, VersionInformation versionInformation) {
        GetGroupVersionRequest getGroupVersionRequest = new GetGroupVersionRequest()
                .withGroupId(groupId)
                .withGroupVersionId(versionInformation.getVersion());

        return awsGreengrassClient.getGroupVersion(getGroupVersionRequest);
    }

    @Override
    public String getFunctionDefinitionVersionArn(String groupId, VersionInformation versionInformation) {
        GetGroupVersionResult getGroupVersionResult = getGroupVersion(groupId, versionInformation);
        GroupVersion groupVersion = getGroupVersionResult.getDefinition();

        return groupVersion.getFunctionDefinitionVersionArn();
    }

    @Override
    public String getLoggerDefinitionVersionArn(String groupId, VersionInformation versionInformation) {
        GetGroupVersionResult getGroupVersionResult = getGroupVersion(groupId, versionInformation);
        GroupVersion groupVersion = getGroupVersionResult.getDefinition();

        return groupVersion.getLoggerDefinitionVersionArn();
    }

    @Override
    public GetFunctionDefinitionResult getFunctionDefinition(String groupId, VersionInformation versionInformation) {
        String functionDefinitionVersionArn = getFunctionDefinitionVersionArn(groupId, versionInformation);

        GetFunctionDefinitionRequest getFunctionDefinitionRequest = new GetFunctionDefinitionRequest()
                .withFunctionDefinitionId(idExtractor.extractId(functionDefinitionVersionArn));
        return awsGreengrassClient.getFunctionDefinition(getFunctionDefinitionRequest);
    }

    @Override
    public GetFunctionDefinitionVersionResult getFunctionDefinitionVersion(String groupId, VersionInformation versionInformation) {
        String functionDefinitionVersionArn = getFunctionDefinitionVersionArn(groupId, versionInformation);

        if (functionDefinitionVersionArn == null) {
            return null;
        }

        GetFunctionDefinitionVersionRequest getFunctionDefinitionVersionRequest = new GetFunctionDefinitionVersionRequest()
                .withFunctionDefinitionId(idExtractor.extractId(functionDefinitionVersionArn))
                .withFunctionDefinitionVersionId(idExtractor.extractVersionId(functionDefinitionVersionArn));
        return awsGreengrassClient.getFunctionDefinitionVersion(getFunctionDefinitionVersionRequest);
    }

    @Override
    public GetLoggerDefinitionVersionResult getLoggerDefinitionVersion(String groupId, VersionInformation versionInformation) {
        String loggerDefinitionVersionArn = getLoggerDefinitionVersionArn(groupId, versionInformation);

        if (loggerDefinitionVersionArn == null) {
            return null;
        }

        GetLoggerDefinitionVersionRequest getLoggerDefinitionVersionRequest = new GetLoggerDefinitionVersionRequest()
                .withLoggerDefinitionId(idExtractor.extractId(loggerDefinitionVersionArn))
                .withLoggerDefinitionVersionId(idExtractor.extractVersionId(loggerDefinitionVersionArn));
        return awsGreengrassClient.getLoggerDefinitionVersion(getLoggerDefinitionVersionRequest);
    }

    @Override
    public void deleteLoggerDefinition(DefinitionInformation definitionInformation) {
        DeleteLoggerDefinitionRequest deleteLoggerDefinitionRequest = new DeleteLoggerDefinitionRequest()
                .withLoggerDefinitionId(definitionInformation.getId());

        awsGreengrassClient.deleteLoggerDefinition(deleteLoggerDefinitionRequest);
    }

    @Override
    public List<DefinitionInformation> listLoggerDefinitions() {
        ListLoggerDefinitionsRequest listLoggerDefinitionsRequest = new ListLoggerDefinitionsRequest();

        List<DefinitionInformation> definitionInformationList = new ResultsIterator<DefinitionInformation>(awsGreengrassClient, listLoggerDefinitionsRequest, ListLoggerDefinitionsResult.class).iterateOverResults();

        return definitionInformationList;
    }

    @Override
    public GetResourceDefinitionVersionResult getResourceDefinitionVersion(String groupId, VersionInformation versionInformation) {
        String resourceDefinitionVersionArn = getResourceDefinitionVersionArn(groupId, versionInformation);

        if (resourceDefinitionVersionArn == null) {
            return null;
        }

        GetResourceDefinitionVersionRequest getResourceDefinitionVersionRequest = new GetResourceDefinitionVersionRequest()
                .withResourceDefinitionId(idExtractor.extractId(resourceDefinitionVersionArn))
                .withResourceDefinitionVersionId(idExtractor.extractVersionId(resourceDefinitionVersionArn));
        return awsGreengrassClient.getResourceDefinitionVersion(getResourceDefinitionVersionRequest);
    }

    @Override
    public List<DefinitionInformation> listResourceDefinitions() {
        ListResourceDefinitionsRequest listResourceDefinitionsRequest = new ListResourceDefinitionsRequest();

        List<DefinitionInformation> definitionInformationList = new ResultsIterator<DefinitionInformation>(awsGreengrassClient, listResourceDefinitionsRequest, ListResourceDefinitionsResult.class).iterateOverResults();

        return definitionInformationList;
    }

    @Override
    public void deleteResourceDefinition(DefinitionInformation definitionInformation) {
        DeleteResourceDefinitionRequest deleteResourceDefinitionRequest = new DeleteResourceDefinitionRequest()
                .withResourceDefinitionId(definitionInformation.getId());

        awsGreengrassClient.deleteResourceDefinition(deleteResourceDefinitionRequest);
    }

    @Override
    public String getResourceDefinitionVersionArn(String groupId, VersionInformation versionInformation) {
        GetGroupVersionResult getGroupVersionResult = getGroupVersion(groupId, versionInformation);
        GroupVersion groupVersion = getGroupVersionResult.getDefinition();

        return groupVersion.getResourceDefinitionVersionArn();
    }

    @Override
    public String getSubscriptionDefinitionVersionArn(String groupId, VersionInformation versionInformation) {
        GetGroupVersionResult getGroupVersionResult = getGroupVersion(groupId, versionInformation);
        GroupVersion groupVersion = getGroupVersionResult.getDefinition();

        return groupVersion.getSubscriptionDefinitionVersionArn();
    }

    @Override
    public GetSubscriptionDefinitionResult getSubscriptionDefinition(String groupId, VersionInformation versionInformation) {
        String subscriptionDefinitionVersionArn = getSubscriptionDefinitionVersionArn(groupId, versionInformation);

        GetSubscriptionDefinitionRequest getSubscriptionDefinitionRequest = new GetSubscriptionDefinitionRequest()
                .withSubscriptionDefinitionId(idExtractor.extractId(subscriptionDefinitionVersionArn));
        return awsGreengrassClient.getSubscriptionDefinition(getSubscriptionDefinitionRequest);
    }

    @Override
    public GetSubscriptionDefinitionVersionResult getSubscriptionDefinitionVersion(String groupId, VersionInformation versionInformation) {
        String subscriptionDefinitionVersionArn = getSubscriptionDefinitionVersionArn(groupId, versionInformation);

        if (subscriptionDefinitionVersionArn == null) {
            return null;
        }

        GetSubscriptionDefinitionVersionRequest getSubscriptionDefinitionVersionRequest = new GetSubscriptionDefinitionVersionRequest()
                .withSubscriptionDefinitionId(idExtractor.extractId(subscriptionDefinitionVersionArn))
                .withSubscriptionDefinitionVersionId(idExtractor.extractVersionId(subscriptionDefinitionVersionArn));
        return awsGreengrassClient.getSubscriptionDefinitionVersion(getSubscriptionDefinitionVersionRequest);
    }

    @Override
    public boolean deleteGroup(String groupId) {
        if (isGroupImmutable(groupId)) {
            // Don't delete a definition for an immutable group
            log.info("Skipping group [" + groupId + "] because it is an immutable group");
            return false;
        }

        try {
            ResetDeploymentsRequest resetDeploymentsRequest = new ResetDeploymentsRequest()
                    .withGroupId(groupId);

            awsGreengrassClient.resetDeployments(resetDeploymentsRequest);
        } catch (AWSGreengrassException e) {
            // Ignore
        }

        DeleteGroupRequest deleteGroupRequest = new DeleteGroupRequest()
                .withGroupId(groupId);
        awsGreengrassClient.deleteGroup(deleteGroupRequest);
        log.info("Deleted group [" + groupId + "]");

        return true;
    }

    private String getDeviceDefinitionVersionArn(String groupId, VersionInformation versionInformation) {
        GetGroupVersionResult getGroupVersionResult = getGroupVersion(groupId, versionInformation);
        GroupVersion groupVersion = getGroupVersionResult.getDefinition();

        return groupVersion.getDeviceDefinitionVersionArn();
    }

    @Override
    public GetDeviceDefinitionVersionResult getDeviceDefinitionVersion(String groupId, VersionInformation versionInformation) {
        String deviceDefinitionVersionArn = getDeviceDefinitionVersionArn(groupId, versionInformation);

        if (deviceDefinitionVersionArn == null) {
            return null;
        }

        GetDeviceDefinitionVersionRequest getDeviceDefinitionVersionRequest = new GetDeviceDefinitionVersionRequest()
                .withDeviceDefinitionId(idExtractor.extractId(deviceDefinitionVersionArn))
                .withDeviceDefinitionVersionId(idExtractor.extractVersionId(deviceDefinitionVersionArn));
        return awsGreengrassClient.getDeviceDefinitionVersion(getDeviceDefinitionVersionRequest);
    }

    @Override
    public GetConnectorDefinitionVersionResult getConnectorDefinitionVersion(String groupId, VersionInformation versionInformation) {
        String connectorDefinitionVersionArn = getConnectorDefinitionVersionArn(groupId, versionInformation);

        return getConnectorDefinitionVersionResult(idExtractor.extractId(connectorDefinitionVersionArn), idExtractor.extractVersionId(connectorDefinitionVersionArn));
    }

    private GetConnectorDefinitionVersionResult getConnectorDefinitionVersionResult(String connectorDefinitionId, String connectorDefinitionVersionId) {
        GetConnectorDefinitionVersionRequest getConnectorDefinitionVersionRequest = new GetConnectorDefinitionVersionRequest()
                .withConnectorDefinitionId(connectorDefinitionId)
                .withConnectorDefinitionVersionId(connectorDefinitionVersionId);
        return awsGreengrassClient.getConnectorDefinitionVersion(getConnectorDefinitionVersionRequest);
    }

    @Override
    public List<DefinitionInformation> listCoreDefinitions() {
        ListCoreDefinitionsRequest listCoreDefinitionsRequest = new ListCoreDefinitionsRequest();

        List<DefinitionInformation> definitionInformationList = new ResultsIterator<DefinitionInformation>(awsGreengrassClient, listCoreDefinitionsRequest, ListCoreDefinitionsResult.class).iterateOverResults();

        return definitionInformationList;
    }

    @Override
    public void deleteCoreDefinition(DefinitionInformation definitionInformation) {
        DeleteCoreDefinitionRequest deleteCoreDefinitionRequest = new DeleteCoreDefinitionRequest()
                .withCoreDefinitionId(definitionInformation.getId());

        awsGreengrassClient.deleteCoreDefinition(deleteCoreDefinitionRequest);
    }

    private boolean isCoreDefinitionImmutable(DefinitionInformation definitionInformation) {
        GetCoreDefinitionVersionResult coreDefinitionVersionResult = getCoreDefinitionVersion(definitionInformation.getId(), definitionInformation.getLatestVersion());

        String coreThingArn = coreDefinitionVersionResult.getDefinition().getCores().get(0).getThingArn();
        return thingHelperProvider.get().isThingArnImmutable(coreThingArn);
    }

    @Override
    public List<DefinitionInformation> listFunctionDefinitions() {
        ListFunctionDefinitionsRequest listFunctionDefinitionsRequest = new ListFunctionDefinitionsRequest();

        List<DefinitionInformation> definitionInformationList = new ResultsIterator<DefinitionInformation>(awsGreengrassClient, listFunctionDefinitionsRequest, ListFunctionDefinitionsResult.class).iterateOverResults();

        return definitionInformationList;
    }

    @Override
    public void deleteFunctionDefinition(DefinitionInformation definitionInformation) {
        DeleteFunctionDefinitionRequest deleteFunctionDefinitionRequest = new DeleteFunctionDefinitionRequest()
                .withFunctionDefinitionId(definitionInformation.getId());

        awsGreengrassClient.deleteFunctionDefinition(deleteFunctionDefinitionRequest);
    }

    @Override
    public List<DefinitionInformation> listSubscriptionDefinitions() {
        ListSubscriptionDefinitionsRequest listSubscriptionDefinitionsRequest = new ListSubscriptionDefinitionsRequest();

        List<DefinitionInformation> definitionInformationList = new ResultsIterator<DefinitionInformation>(awsGreengrassClient, listSubscriptionDefinitionsRequest, ListSubscriptionDefinitionsResult.class).iterateOverResults();

        return definitionInformationList;
    }

    @Override
    public void deleteSubscriptionDefinition(DefinitionInformation definitionInformation) {
        DeleteSubscriptionDefinitionRequest deleteSubscriptionDefinitionRequest = new DeleteSubscriptionDefinitionRequest()
                .withSubscriptionDefinitionId(definitionInformation.getId());

        awsGreengrassClient.deleteSubscriptionDefinition(deleteSubscriptionDefinitionRequest);
    }

    @Override
    public List<DefinitionInformation> listDeviceDefinitions() {
        ListDeviceDefinitionsRequest listDeviceDefinitionsRequest = new ListDeviceDefinitionsRequest();

        List<DefinitionInformation> definitionInformationList = new ResultsIterator<DefinitionInformation>(awsGreengrassClient, listDeviceDefinitionsRequest, ListDeviceDefinitionsResult.class).iterateOverResults();

        return definitionInformationList;
    }

    @Override
    public void deleteDeviceDefinition(DefinitionInformation definitionInformation) {
        DeleteDeviceDefinitionRequest deleteDeviceDefinitionRequest = new DeleteDeviceDefinitionRequest()
                .withDeviceDefinitionId(definitionInformation.getId());

        awsGreengrassClient.deleteDeviceDefinition(deleteDeviceDefinitionRequest);
    }

    @Override
    public boolean groupExists(String groupId) {
        GetGroupRequest getGroupRequest = new GetGroupRequest()
                .withGroupId(groupId);

        try {
            awsGreengrassClient.getGroup(getGroupRequest);

            return true;
        } catch (AWSGreengrassException e) {
            if (e.getStatusCode() == 404) {
                return false;
            }

            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public boolean isGroupImmutable(String groupId) {
        if (!groupExists(groupId)) {
            return false;
        }

        VersionInformation latestGroupVersion = getLatestGroupVersion(groupId);

        if (latestGroupVersion == null) {
            return false;
        }

        if (!coreDefinitionVersionExists(latestGroupVersion)) {
            return false;
        }

        GetCoreDefinitionVersionResult coreDefinitionVersionResult = getCoreDefinitionVersion(groupId, latestGroupVersion);
        String coreThingArn = coreDefinitionVersionResult.getDefinition().getCores().get(0).getThingArn();

        ThingHelper thingHelper = thingHelperProvider.get();

        return thingHelper.isThingArnImmutable(coreThingArn);
    }

    private boolean coreDefinitionVersionExists(VersionInformation latestGroupVersion) {
        try {
            getCoreDefinitionVersion(latestGroupVersion.getId(), latestGroupVersion);

            return true;
        } catch (AWSGreengrassException e) {
            if (e.getStatusCode() == 404) {
                return false;
            }

            throw new UnsupportedOperationException(e);
        }
    }

    private void sortDeployments(List<Deployment> deploymentsList) {
        Collections.sort(deploymentsList, Comparator.comparing(Deployment::getCreatedAt));
    }

    private void sortGroupVersionInformation(List<VersionInformation> versionInformationList) {
        Collections.sort(versionInformationList, Comparator.comparing(VersionInformation::getCreationTimestamp));
    }

    private void sortGroupInformation(List<GroupInformation> groupInformationList) {
        Collections.sort(groupInformationList, Comparator.comparing(GroupInformation::getName));
    }
}
