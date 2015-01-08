package de.holisticon.dropwizard.deltaspike.example;

import com.google.common.io.Resources;
import de.holisticon.dropwizard.deltaspike.DeltaspikeManagedBundle;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.io.File;

@ApplicationScoped
public class DeltaspikeExampleApplication extends Application<DeltaspikeExampleApplication.Config> {

    @Dependent
    public static class Foo {

    }

    @Inject
    private Foo foo;


    @Inject
    private DummyHealthCheck dummyHealthCheck;


    @Inject
    private DummyResource dummyResource;



    @Override
    public void run(final Config config, final Environment environment) throws Exception {
        environment.healthChecks().register("dummy", dummyHealthCheck);
        environment.jersey().register(dummyResource);
    }

    @Override
    public void initialize(Bootstrap<Config> bootstrap) {
        bootstrap.addBundle(new DeltaspikeManagedBundle());
    }

    public static class Config extends Configuration {
        // empty
    }

    public static void main(String... _unused) throws Exception {
        new DeltaspikeExampleApplication().run("server", new File(Resources.getResource("example.yaml").toURI()).getAbsolutePath());
    }
}