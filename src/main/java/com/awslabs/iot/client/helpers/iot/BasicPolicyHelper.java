package com.awslabs.iot.client.helpers.iot;

import com.amazonaws.services.iot.AWSIotClient;
import com.amazonaws.services.iot.model.*;
import com.awslabs.aws.iot.resultsiterator.ResultsIterator;
import com.awslabs.iot.client.helpers.iot.interfaces.PolicyHelper;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class BasicPolicyHelper implements PolicyHelper {
    private static final String ALLOW_ALL_POLICY_DOCUMENT = "{ \"Statement\": [ { \"Action\": \"iot:*\", \"Resource\": \"*\", \"Effect\": \"Allow\" } ], \"Version\": \"2012-10-17\" }";
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(BasicPolicyHelper.class);

    @Inject
    AWSIotClient awsIotClient;

    @Inject
    public BasicPolicyHelper() {
    }

    @Override
    public void createAllowAllPolicy(String clientName) {
        try {
            CreatePolicyRequest createPolicyRequest = new CreatePolicyRequest()
                    .withPolicyName(clientName)
                    .withPolicyDocument(ALLOW_ALL_POLICY_DOCUMENT);
            awsIotClient.createPolicy(createPolicyRequest);
        } catch (Exception e) {
            log.info("Failed to create the policy.  Maybe it already exists?  If not, do you have the correct permissions to call iot:CreatePolicy?");
            log.info("Continuing anyway...");
        }
    }

    @Override
    public void attachPolicyToCertificate(String clientName, String certificateArn) {
        try {
            AttachPolicyRequest attachPolicyRequest = new AttachPolicyRequest()
                    .withPolicyName(clientName)
                    .withTarget(certificateArn);
            awsIotClient.attachPolicy(attachPolicyRequest);
        } catch (Exception e) {
            log.info("Failed to attach the policy to your certificate.  Do you have the correct permissions to call iot:AttachPrincipalPolicy?");
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public List<Policy> listPolicies() {
        List<Policy> policies = new ResultsIterator<Policy>(awsIotClient, ListPoliciesRequest.class).iterateOverResults();

        return policies;
    }

    @Override
    public List<String> listPolicyNames() {
        List<Policy> policies = listPolicies();

        List<String> policyNames = new ArrayList<>();

        for (Policy policy : policies) {
            policyNames.add(policy.getPolicyName());
        }

        return policyNames;
    }

    @Override
    public List<String> listPolicyPrincipals(String policyName) {
        ListPolicyPrincipalsRequest listPolicyPrincipalsRequest = new ListPolicyPrincipalsRequest()
                .withPolicyName(policyName);

        List<String> principals = new ResultsIterator<String>(awsIotClient, listPolicyPrincipalsRequest).iterateOverResults();

        return principals;
    }

    @Override
    public void deletePolicy(String policyName) {
        List<String> policyPrincipals = listPolicyPrincipals(policyName);

        for (String policyPrincipal : policyPrincipals) {
            detachPolicy(policyPrincipal, policyName);
        }

        DeletePolicyRequest deletePolicyRequest = new DeletePolicyRequest()
                .withPolicyName(policyName);

        try {
            log.info("Attempting to delete policy [" + policyName + "]");
            awsIotClient.deletePolicy(deletePolicyRequest);
        } catch (UnauthorizedException e) {
            log.info("You are not allowed to delete policy [" + policyName + "]");
        } catch (ResourceNotFoundException e) {
            log.info("The policy was not found [" + policyName + "]");
        } catch (DeleteConflictException e) {
            log.info("Policy has multiple versions, attempting to delete all versions");

            deletePolicyAndPolicyVersions(policyName);
        }
    }

    private void deletePolicyAndPolicyVersions(String policyName) {
        ListPolicyVersionsRequest listPolicyVersionsRequest = new ListPolicyVersionsRequest()
                .withPolicyName(policyName);

        ListPolicyVersionsResult listPolicyVersionsResult = awsIotClient.listPolicyVersions(listPolicyVersionsRequest);
        List<PolicyVersion> policyVersions = listPolicyVersionsResult.getPolicyVersions();

        // Delete the policies
        for (PolicyVersion policyVersion : policyVersions) {
            if (policyVersion.isDefaultVersion()) {
                // Default policy, can't delete it
                continue;
            }

            DeletePolicyVersionRequest deletePolicyVersionRequest = new DeletePolicyVersionRequest()
                    .withPolicyVersionId(policyVersion.getVersionId())
                    .withPolicyName(policyName);
            awsIotClient.deletePolicyVersion(deletePolicyVersionRequest);
        }

        // Delete the policy
        DeletePolicyRequest deletePolicyRequest = new DeletePolicyRequest()
                .withPolicyName(policyName);

        awsIotClient.deletePolicy(deletePolicyRequest);
    }

    @Override
    public void detachPolicy(String principal, String policyName) {
        DetachPolicyRequest detachPolicyRequest = new DetachPolicyRequest()
                .withTarget(principal)
                .withPolicyName(policyName);

        log.info("Attempting to detach principal [" + principal + "] from policy [" + policyName + "]");
        awsIotClient.detachPolicy(detachPolicyRequest);
    }

    @Override
    public List<Policy> listPrincipalPolicies(String principal) {
        ListPrincipalPoliciesRequest listPrincipalPoliciesRequest = new ListPrincipalPoliciesRequest()
                .withPrincipal(principal);

        List<Policy> policies = new ResultsIterator<Policy>(awsIotClient, listPrincipalPoliciesRequest).iterateOverResults();

        return policies;
    }
}
