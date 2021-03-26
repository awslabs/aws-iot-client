package com.awslabs.iot.client.helpers;

import io.vavr.collection.List;
import io.vavr.collection.Stream;
import org.jline.reader.Candidate;

public interface CandidateHelper {
    List<Candidate> getCandidates(Stream<String> strings);
}
