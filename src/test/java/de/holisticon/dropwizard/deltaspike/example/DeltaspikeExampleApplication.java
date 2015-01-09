package de.holisticon.dropwizard.deltaspike.example;

import com.google.common.io.Resources;
import de.holisticon.dropwizard.deltaspike.DeltaspikeBundle;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.io.File;

import static org.slf4j.LoggerFactory.getLogger;

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

    private final Logger logger = getLogger(this.getClass());


    @Override
    public void run(final Config config, final Environment environment) throws Exception {
        logger.error("------------------- application-run-1");

        environment.healthChecks().register("dummy", dummyHealthCheck);
        environment.jersey().register(dummyResource);

        logger.error("------------------- application-run-2               ----");
    }

    @Override
    public void initialize(Bootstrap<Config> bootstrap) {
        logger.error("------------------- application-init-1");
        bootstrap.addBundle(new DeltaspikeBundle());
        logger.error("------------------- application-init-2");
    }

    public static class Config extends Configuration {
        // empty
    }

    public static void main(String... _unused) throws Exception {
        new DeltaspikeExampleApplication().run("server", new File(Resources.getResource("example.yaml").toURI()).getAbsolutePath());
    }
}