package com.company;

import com.company.resources.dropwizardExampleResource;
import com.company.resources.indexResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.sql.SQLException;

public class dropwizardExampleApplication extends Application<dropwizardExampleConfiguration> {

    public static void main(final String[] args) throws Exception {
        new dropwizardExampleApplication().run(args);
    }

    @Override
    public String getName() {
        return "dropwizardExample";
    }

    @Override
    public void initialize(final Bootstrap<dropwizardExampleConfiguration> bootstrap) {
    }

    @Override
    public void run(final dropwizardExampleConfiguration configuration,
                    final Environment environment) throws SQLException {
        environment.jersey().register(new dropwizardExampleResource());
        environment.jersey().register(new indexResource());
    }

}
