package com.awslabs.iot.client.helpers;

import org.jline.reader.Candidate;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class BasicCandidateHelper implements CandidateHelper {
    @Inject
    public BasicCandidateHelper() {
    }

    @Override
    public List<Candidate> getCandidates(List<String> strings) {
        return strings
                .stream()
                .map(Candidate::new)
                .collect(Collectors.toList());
    }
}
