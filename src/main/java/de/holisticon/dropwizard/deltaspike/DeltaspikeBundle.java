package de.holisticon.dropwizard.deltaspike;

import io.dropwizard.Bundle;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
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

    private final Logger logger = getLogger(this.getClass());

    private final CdiContainer cdiContainer = CdiContainerLoader.getCdiContainer();


    @Override
    public void run(T configuration, Environment environment) throws Exception {

    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        cdiContainer.boot();

        startContexts();

        registerShutdownHook();

        BeanProvider.injectFields(bootstrap.getApplication());
    }


    private void startContexts() {
        cdiContainer.getContextControl().startContext(RequestScoped.class);
        cdiContainer.getContextControl().startContext(SessionScoped.class);
        cdiContainer.getContextControl().startContext(ApplicationScoped.class);
    }

    private void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public synchronized void run() {
                try {
                    cdiContainer.getContextControl().stopContexts();
                    cdiContainer.shutdown();
                    logger.info("shut down container");
                } catch (final NullPointerException e) {
                    // weld lifecycle already ended
                    logger.debug("weld has been already shut down", e);
                }
            }
        });
    }
}
