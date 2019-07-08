package com.awslabs.iot.client.helpers;

import org.jline.reader.Candidate;

import java.util.List;

public interface CandidateHelper {
    List<Candidate> getCandidates(List<String> strings);
}
