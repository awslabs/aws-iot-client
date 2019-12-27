package com.awslabs.iot.client.helpers.iam;

import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClient;
import com.amazonaws.services.identitymanagement.model.*;
import com.awslabs.aws.iot.resultsiterator.ResultsIterator;
import com.awslabs.iot.client.helpers.iam.interfaces.IamHelper;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class BasicIamHelper implements IamHelper {
    @Inject
    AmazonIdentityManagementClient amazonIdentityManagementClient;

    @Inject
    public BasicIamHelper() {
    }

    @Override
    public List<Role> listRoles() {
        List<Role> roles = new ResultsIterator<Role>(amazonIdentityManagementClient, ListRolesRequest.class).iterateOverResults();

        return roles;

    }

    @Override
    public List<String> listRoleNames() {
        List<Role> roles = listRoles();

        List<String> roleNames = new ArrayList<>();

        for (Role role : roles) {
            roleNames.add(role.getRoleName());
        }

        return roleNames;
    }

    @Override
    public String getRoleArn(String roleName) {
        GetRoleRequest getRoleRequest = new GetRoleRequest()
                .withRoleName(roleName);

        GetRoleResult getRoleResult = amazonIdentityManagementClient.getRole(getRoleRequest);

        Role role = getRoleResult.getRole();

        if (role == null) {
            return null;
        }

        return role.getArn();
    }
}
