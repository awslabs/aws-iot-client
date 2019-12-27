package com.awslabs.iot.client.helpers.iot;

import com.amazonaws.services.iot.AWSIotClient;
import com.amazonaws.services.iot.model.ListTopicRulesRequest;
import com.amazonaws.services.iot.model.ListTopicRulesResult;
import com.amazonaws.services.iot.model.TopicRuleListItem;
import com.awslabs.aws.iot.resultsiterator.ResultsIterator;
import com.awslabs.iot.client.helpers.iot.interfaces.RuleHelper;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class BasicRuleHelper implements RuleHelper {
    @Inject
    AWSIotClient awsIotClient;

    @Inject
    public BasicRuleHelper() {
    }

    @Override
    public List<TopicRuleListItem> listTopicRules() {
        List<TopicRuleListItem> topicRules = new ResultsIterator<TopicRuleListItem>(awsIotClient, ListTopicRulesRequest.class).iterateOverResults();

        return topicRules;
    }

    @Override
    public List<String> listTopicRuleNames() {
        List<TopicRuleListItem> topicRules = listTopicRules();

        List<String> topicRuleNames = new ArrayList<>();

        for (TopicRuleListItem topicRuleListItem : topicRules) {
            topicRuleNames.add(topicRuleListItem.getRuleName());
        }

        return topicRuleNames;
    }
}
