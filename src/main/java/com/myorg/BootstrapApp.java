package com.myorg;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

import java.util.Objects;

public class BootstrapApp {

    public static void main(String[] args) {
        App app = new App();
        String region = (String) app.getNode().tryGetContext("region");
        Objects.requireNonNull(region, "please set region in context!");

        String accountId = (String) app.getNode().tryGetContext("accountId");
        Objects.requireNonNull(accountId, "please set accountId in context!");

        Environment awsEnvironment = makeEnv(accountId, region);

        Stack bootstrapStack = new Stack(app, "Bootstrap", StackProps.builder()
                .env(awsEnvironment)
                .build());

        app.synth();
    }

    static Environment makeEnv(String account, String region) {
        return Environment.builder()
                .account(account)
                .region(region)
                .build();
    }
}
