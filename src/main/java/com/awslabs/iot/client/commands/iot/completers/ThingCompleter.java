package com.awslabs.iot.client.commands.iot.completers;

import com.awslabs.iot.client.completers.DynamicStringsCompleter;
import com.awslabs.iot.client.helpers.CandidateHelper;
import com.awslabs.iot.helpers.interfaces.V1ThingHelper;
import org.jline.reader.Candidate;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;

public class ThingCompleter extends DynamicStringsCompleter {
    @Inject
    Provider<V1ThingHelper> thingHelperProvider;
    @Inject
    CandidateHelper candidateHelper;

    @Inject
    public ThingCompleter() {
    }

    @Override
    public List<Candidate> getStrings() {
        return candidateHelper.getCandidates(thingHelperProvider.get().listThingNames());
    }
}
