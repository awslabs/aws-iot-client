package com.awslabs.iot.client.applications;

import com.beust.jcommander.Parameter;

public class Arguments {
    private final String LONG_DANGEROUS_MODE_OPTION_1 = "--dangerousMode";
    private final String LONG_DANGEROUS_MODE_OPTION_2 = "--dangerous-mode";

    @Parameter(names = {LONG_DANGEROUS_MODE_OPTION_1, LONG_DANGEROUS_MODE_OPTION_2})
    public boolean dangerousMode = false;
}
