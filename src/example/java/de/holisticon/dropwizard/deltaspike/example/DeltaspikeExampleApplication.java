package de.holisticon.dropwizard.deltaspike.example;

import com.codahale.metrics.health.HealthCheck;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.io.Resources;
import de.holisticon.dropwizard.deltaspike.DeltaspikeBundle;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import jersey.repackaged.com.google.common.base.Objects;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.io.File;

import static org.slf4j.LoggerFactory.getLogger;

@ApplicationScoped
public class DeltaspikeExampleApplication extends Application<DeltaspikeExampleApplication.Config> {

    @Inject
    private FooLogger.Foo foo;

    @Inject
    private Event<FooLogger.Foo> fooEvent;

    @Inject
    private DummyResource dummyResource;

    private final Logger logger = getLogger(this.getClass());

    @Override
    public void run(final Config config, final Environment environment) throws Exception {
        logger.error("------------------- application-run-1");
        fooEvent.fire(foo);

        environment.healthChecks().register("dummy", new HealthCheck() {
            @Override
            protected Result check() throws Exception {
                return Result.healthy("dummy");
            }
        });
        environment.jersey().register(dummyResource);

        logger.error("------------------- application-run-2");
    }

    @Override
    public void initialize(final Bootstrap<Config> bootstrap) {
        // add DeltaspikeBundle here ... nothing else required.
        bootstrap.addBundle(new DeltaspikeBundle(Config.class));
    }

    /**
     * Simple Configuration class for this example application.
     */
    public static class Config extends Configuration {
        @JsonProperty
        public String exampleName;

        @Override
        public String toString() {
            return Objects.toStringHelper(this).add("exampleName", exampleName).toString();
        }
    }

    /**
     * Simplified main() to run this example from IDE/maven.
     *
     * @param _unused
     * @throws Exception
     */
    public static void main(String... _unused) throws Exception {
        new DeltaspikeExampleApplication().run("server", new File(Resources.getResource("example.yaml").toURI()).getAbsolutePath());
    }
}
