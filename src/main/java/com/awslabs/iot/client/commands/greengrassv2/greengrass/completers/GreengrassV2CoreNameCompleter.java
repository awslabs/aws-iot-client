package com.awslabs.iot.client.commands.greengrassv2.greengrass.completers;

import com.awslabs.iot.client.completers.DynamicStringsCompleter;
import com.awslabs.iot.client.helpers.CandidateHelper;
import com.awslabs.iot.helpers.interfaces.GreengrassV1Helper;
import com.awslabs.iot.helpers.interfaces.GreengrassV2Helper;
import io.vavr.collection.List;
import org.jline.reader.Candidate;
import software.amazon.awssdk.services.greengrass.model.GroupInformation;
import software.amazon.awssdk.services.greengrassv2.model.CoreDevice;

import javax.inject.Inject;

public class GreengrassV2CoreNameCompleter extends DynamicStringsCompleter {
    @Inject
    GreengrassV2Helper greengrassV2Helper;
    @Inject
    CandidateHelper candidateHelper;

    @Inject
    public GreengrassV2CoreNameCompleter() {
    }

    @Override
    public List<Candidate> getStrings() {
        return candidateHelper.getCandidates(greengrassV2Helper.getAllCoreDevices().map(CoreDevice::coreDeviceThingName));
    }
}
