package de.holisticon.dropwizard.deltaspike;

import static org.slf4j.LoggerFactory.getLogger;

import com.google.common.base.Preconditions;
import io.dropwizard.Application;
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
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.spi.Bean;

/**
 * Dropwizard bundle that starts and initializes a deltaspike CDIContainer.
 *
 * @param <T> the type of configuration used by application.
 */
public class DeltaspikeBundle<T extends Configuration> implements ConfiguredBundle<T> {

    private final Logger logger = getLogger(this.getClass());

    private final Class<T> configurationClass;

    private Application<T> application;

    static Bean<? extends Configuration> configurationBean;

    /**
     * Create new bundle instance.
     *
     * @param configurationClass concrete type for T, needed to register generic beans with CDI.
     */
    public DeltaspikeBundle(final Class<T> configurationClass) {
        this.configurationClass = Preconditions.checkNotNull(configurationClass);
    }

    @Override
    public void initialize(final Bootstrap<?> bootstrap) {
        application = (Application<T>) bootstrap.getApplication();
    }

    @Override
    public void run(T configuration, Environment environment) throws Exception {

        logger.error("------------------- bundle-run-1");

        final CdiContainer cdiContainer = bootCdiContainer();

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
        logger.error("------------------- bundle-run-2");
    }

    /**
     * Boot Container and start contexts.
     */
    public static CdiContainer bootCdiContainer() {
        final CdiContainer cdiContainer = CdiContainerLoader.getCdiContainer();
        cdiContainer.boot();
        cdiContainer.getContextControl().startContext(RequestScoped.class);
        cdiContainer.getContextControl().startContext(SessionScoped.class);
        cdiContainer.getContextControl().startContext(ApplicationScoped.class);
        cdiContainer.getContextControl().startContext(ConversationScoped.class);

        return cdiContainer;
    }


}
