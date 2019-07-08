package com.awslabs.iot.client.helpers.iot.interfaces;

import com.amazonaws.services.iot.model.TopicRuleListItem;

import java.util.List;

public interface RuleHelper {
    List<TopicRuleListItem> listTopicRules();

    List<String> listTopicRuleNames();
}
