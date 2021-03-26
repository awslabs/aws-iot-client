package com.awslabs.iot.client.commands.iam.completers;

import com.awslabs.iam.data.RoleName;
import com.awslabs.iam.helpers.interfaces.IamHelper;
import com.awslabs.iot.client.completers.DynamicStringsCompleter;
import com.awslabs.iot.client.helpers.CandidateHelper;
import io.vavr.collection.List;
import org.jline.reader.Candidate;

import javax.inject.Inject;

public class RoleCompleter extends DynamicStringsCompleter {
    @Inject
    IamHelper iamHelper;
    @Inject
    CandidateHelper candidateHelper;

    @Inject
    public RoleCompleter() {
    }

    @Override
    public List<Candidate> getStrings() {
        return candidateHelper.getCandidates(iamHelper.getRoleNames()
                .map(RoleName::getName));
    }
}
