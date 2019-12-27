package com.awslabs.iot.client.helpers.iot;

import com.amazonaws.services.iot.AWSIotClient;
import com.amazonaws.services.iot.model.DeleteThingGroupRequest;
import com.amazonaws.services.iot.model.GroupNameAndArn;
import com.amazonaws.services.iot.model.ListThingGroupsRequest;
import com.amazonaws.services.iot.model.ListThingGroupsResult;
import com.awslabs.aws.iot.resultsiterator.ResultsIterator;
import com.awslabs.iot.client.helpers.iot.interfaces.ThingGroupHelper;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class BasicThingGroupHelper implements ThingGroupHelper {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(BasicThingGroupHelper.class);
    @Inject
    AWSIotClient awsIotClient;

    @Inject
    public BasicThingGroupHelper() {
    }

    @Override
    public List<GroupNameAndArn> listThingGroups() {
        List<GroupNameAndArn> groupNamesAndArns = new ResultsIterator<GroupNameAndArn>(awsIotClient, ListThingGroupsRequest.class).iterateOverResults();

        return groupNamesAndArns;
    }

    @Override
    public void deleteThingGroup(String thingGroupName) {
        awsIotClient.deleteThingGroup(new DeleteThingGroupRequest().withThingGroupName(thingGroupName));
    }
}
