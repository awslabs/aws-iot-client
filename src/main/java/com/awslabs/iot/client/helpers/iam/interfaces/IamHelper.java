package com.awslabs.iot.client.helpers.iam.interfaces;

import com.amazonaws.services.identitymanagement.model.Role;

import java.util.List;

public interface IamHelper {
    List<Role> listRoles();

    List<String> listRoleNames();

    String getRoleArn(String roleName);
}
