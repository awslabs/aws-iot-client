package com.awslabs.iot.client.helpers;

import io.vavr.collection.List;
import io.vavr.collection.Stream;
import org.jline.reader.Candidate;

import javax.inject.Inject;

public class BasicCandidateHelper implements CandidateHelper {
    @Inject
    public BasicCandidateHelper() {
    }

    @Override
    public List<Candidate> getCandidates(Stream<String> strings) {
        return strings
                .map(Candidate::new)
                .toList();
    }
}
