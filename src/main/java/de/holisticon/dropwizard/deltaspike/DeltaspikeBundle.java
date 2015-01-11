package de.holisticon.dropwizard.deltaspike;

import static org.slf4j.LoggerFactory.getLogger;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.cdise.servlet.CdiServletRequestListener;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.slf4j.Logger;

import javax.enterprise.inject.spi.Bean;
import javax.inject.Inject;

/**
 * Dropwizard bundle that starts and initializes a deltaspike CDIContainer.
 *
 * @param <T> the type of configuration used by application.
 */
@SuppressWarnings("CdiManagedBeanInconsistencyInspection")
public class DeltaspikeBundle<T extends Configuration> implements ConfiguredBundle<T> {


    public static class Builder<T extends Configuration>  {

        private boolean autoConfig;

        public Builder enableAutoConfig() {
            this.autoConfig = true;
            return this;
        }

        public DeltaspikeBundle<T> build() {
            return new DeltaspikeBundle<T>(autoConfig);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private final Logger logger = getLogger(this.getClass());
    private final boolean autoConfig;

    private Application<T> application;

    static Bean<? extends Configuration> configurationBean;

    @Inject
    private DropwizardCdiExtension dropwizardCdiExtension;


    private DeltaspikeBundle(boolean autoConfig) {

        this.autoConfig = autoConfig;
    }

    @Override
    public void initialize(final Bootstrap<?> bootstrap) {
        application = (Application<T>) bootstrap.getApplication();
    }

    @Override
    public void run(final T configuration, final Environment environment) throws Exception {
        logger.error("------------------- bundle-run-1");

        final CdiContainer cdiContainer = bootCdiContainer();

        BeanProvider.injectFields(this);

        logger.error("------------------- {}", dropwizardCdiExtension);
        logger.error("------------------- {}", dropwizardCdiExtension.getNames());


        BeanProvider.injectFields(application);
        logger.info("-------- injecting into application, so application.run() can use CDI instances.");

        // shutdown CDI Container on server shutdown via Managed.
        environment.lifecycle().manage(new Managed() {
            @Override
            public void start() throws Exception {
                // empty
            }

            @Override
            public void stop() throws Exception {
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

        environment.servlets();
        environment.servlets().addServletListeners(new CdiServletRequestListener());

        logger.error("------------------- bundle-run-2");
    }

    /**
     * Boot Container and start contexts.
     */
    public static CdiContainer bootCdiContainer() {
        final CdiContainer cdiContainer = CdiContainerLoader.getCdiContainer();
        cdiContainer.boot();
        cdiContainer.getContextControl().startContexts();

        return cdiContainer;
    }


}
