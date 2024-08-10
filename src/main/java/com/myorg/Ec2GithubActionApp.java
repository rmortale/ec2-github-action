package com.myorg;

import software.amazon.awscdk.App;

public final class Ec2GithubActionApp {
    public static void main(final String[] args) {
        App app = new App();

        new Ec2GithubActionStack(app, "Ec2GithubActionStack");

        app.synth();
    }
}
