package com.awslabs.iot.client.helpers.iot.interfaces;

import com.amazonaws.services.iot.model.GroupNameAndArn;

import java.util.List;

public interface ThingGroupHelper {
    List<GroupNameAndArn> listThingGroups();

    void deleteThingGroup(String thingGroupName);
}
