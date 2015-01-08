package de.holisticon.dropwizard.deltaspike.example;

import com.google.common.io.Resources;
import de.holisticon.dropwizard.deltaspike.DeltaspikeBundle;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.inject.Inject;
import java.io.File;


public class DeltaspikeExampleApplication extends Application<DeltaspikeExampleApplication.Config> {

    private DummyHealthCheck dummyHealthCheck = new DummyHealthCheck();

    private DummyResource dummyResource = new DummyResource();

    @Override
    public void run(final Config config, final Environment environment) throws Exception {
        environment.healthChecks().register("dummy", dummyHealthCheck);
        environment.jersey().register(dummyResource);
    }

    @Override
    public void initialize(Bootstrap<Config> bootstrap) {
        bootstrap.addBundle(new DeltaspikeBundle());
    }

    public static class Config extends Configuration {
        // empty
    }

    public static void main(String... _unused) throws Exception {
        new DeltaspikeExampleApplication().run("server", new File(Resources.getResource("example.yaml").toURI()).getAbsolutePath());
    }
}