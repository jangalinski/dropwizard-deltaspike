package de.holisticon.dropwizard.deltaspike;

import io.dropwizard.Application;
import io.dropwizard.Bundle;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.spi.CDI;

import static org.slf4j.LoggerFactory.getLogger;

public class DeltaspikeBundle<T extends Configuration> implements ConfiguredBundle<T> {

    private final CdiContainer cdiContainer = CdiContainerLoader.getCdiContainer();
    private final Logger logger = getLogger(this.getClass());


    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        logger.error("------------------- bundle-init-1");
        cdiContainer.boot();

        cdiContainer.getContextControl().startContext(RequestScoped.class);
        cdiContainer.getContextControl().startContext(SessionScoped.class);
        cdiContainer.getContextControl().startContext(ApplicationScoped.class);

        BeanProvider.injectFields(bootstrap.getApplication());
        logger.error("------------------- bundle-init-2");
    }

    @Override
    public void run(T configuration, Environment environment) throws Exception {
        logger.error("------------------- bundle-run-1");
        environment.lifecycle().manage(new Managed() {
            @Override
            public void start() throws Exception {
                logger.error("------------------- managed-start-1");
            }

            @Override
            public void stop() throws Exception {
                logger.error("------------------- managed-stop-1");

                try {
                    cdiContainer.getContextControl().stopContexts();
                    cdiContainer.shutdown();
                    logger.info("shut down container");
                } catch (final NullPointerException e) {
                    // weld lifecycle already ended
                    logger.debug("weld has been already shut down", e);
                }
                logger.error("------------------- managed-stop-2");
            }
        });
        logger.error("------------------- bundle-run-2");
    }


}
