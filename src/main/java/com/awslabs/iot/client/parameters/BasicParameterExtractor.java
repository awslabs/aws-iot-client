package com.awslabs.iot.client.parameters;

import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import io.vavr.collection.List;

import javax.inject.Inject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BasicParameterExtractor implements ParameterExtractor {
    private static final Pattern PATTERN = Pattern.compile("([^\"]\\S*|\".+?\")\\s*");

    @Inject
    public BasicParameterExtractor() {
    }

    @Override
    public List<String> getParameters(String input) {
        // From: http://stackoverflow.com/questions/7804335/split-string-on-spaces-in-java-except-if-between-quotes-i-e-treat-hello-wor
        List<String> parameters = List.empty();

        Matcher m = PATTERN.matcher(input);

        // Get all of the matches and put them in a list
        while (m.find()) {
            // Make sure we remove the double quotes around the parameter
            parameters = parameters.append(m.group(1).replace("\"", ""));
        }

        // Did we get any input?
        if (parameters.size() > 0) {
            // Yes, remove the first parameter since it is just the command name
            parameters = parameters.removeAt(0);
        }

        return parameters;
    }
}
