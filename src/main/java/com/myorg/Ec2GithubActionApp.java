package com.myorg;

import com.myorg.cdk.AppEnv;
import software.amazon.awscdk.*;
import software.amazon.awscdk.services.ec2.SecurityGroup;
import software.amazon.awscdk.services.ec2.SecurityGroupProps;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.ec2.VpcLookupOptions;

import java.util.Objects;

import static com.myorg.BootstrapApp.makeEnv;

public final class Ec2GithubActionApp {

    public static void main(final String[] args) {
        App app = new App();

        String environmentName = (String) app.getNode().tryGetContext("environmentName");
        Objects.requireNonNull(environmentName, "context variable 'environmentName' must not be null");

        String applicationName = (String) app.getNode().tryGetContext("applicationName");
        Objects.requireNonNull(applicationName, "context variable 'applicationName' must not be null");

        String region = (String) app.getNode().tryGetContext("region");
        Objects.requireNonNull(region, "please set region in context!");

        String accountId = (String) app.getNode().tryGetContext("accountId");
        Objects.requireNonNull(accountId, "please set accountId in context!");

        Environment awsEnvironment = makeEnv(accountId, region);

        AppEnv appEnv = new AppEnv(applicationName, environmentName);

        Stack ec2Stack = new Stack(app, "Ec2Stack", StackProps.builder()
                .stackName(appEnv.prefix("ec2Stack"))
                .env(awsEnvironment)
                .build());

        var vpc = Vpc.fromLookup(ec2Stack, "VPC", VpcLookupOptions.builder()
                .isDefault(true)
                .build());

        var ec2sg = new SecurityGroup(ec2Stack, "SG", SecurityGroupProps.builder()
                .vpc(vpc)
                .allowAllOutbound(true)
                .build());

        new CfnOutput(ec2Stack, "output", CfnOutputProps.builder().value(ec2sg.getSecurityGroupId()).build());

        app.synth();
    }


}
