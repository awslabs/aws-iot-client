package com.awslabs.iot.client.commands.iot.completers;

import com.awslabs.iot.client.completers.DynamicStringsCompleter;
import com.awslabs.iot.client.helpers.CandidateHelper;
import com.awslabs.iot.client.helpers.iot.interfaces.RuleHelper;
import org.jline.reader.Candidate;

import javax.inject.Inject;
import java.util.List;

public class RuleCompleter extends DynamicStringsCompleter {
    @Inject
    RuleHelper ruleHelper;
    @Inject
    CandidateHelper candidateHelper;

    @Inject
    public RuleCompleter() {
    }

    @Override
    public List<Candidate> getStrings() {
        return candidateHelper.getCandidates(ruleHelper.listTopicRuleNames());
    }
}
