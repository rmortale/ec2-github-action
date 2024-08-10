package com.myorg.cdk;

import lombok.RequiredArgsConstructor;
import software.amazon.awscdk.Tags;
import software.constructs.IConstruct;

@RequiredArgsConstructor
public class AppEnv {

    private final String applicationName;
    private final String environmentName;

    private String sanitize(String environmentName) {
        return environmentName.replaceAll("[^a-zA-Z0-9-]", "");
    }

    @Override
    public String toString() {
        return sanitize(environmentName + "-" + applicationName);
    }

    /**
     * Prefixes a string with the application name and environment name.
     */
    public String prefix(String string) {
        return this + "-" + string;
    }

    /**
     * Prefixes a string with the application name and environment name. Returns only the last <code>characterLimit</code>
     * characters from the name.
     */
    public String prefix(String string, int characterLimit) {
        String name = this + "-" + string;
        if (name.length() <= characterLimit) {
            return name;
        }
        return name.substring(name.length() - characterLimit);
    }

    public void tag(IConstruct construct) {
        Tags.of(construct).add("environment", environmentName);
        Tags.of(construct).add("application", applicationName);
    }
}
