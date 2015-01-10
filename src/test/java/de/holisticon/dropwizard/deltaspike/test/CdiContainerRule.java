package de.holisticon.dropwizard.deltaspike.test;

import de.holisticon.dropwizard.deltaspike.DeltaspikeBundle;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.junit.rules.ExternalResource;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class CdiContainerRule extends ExternalResource {

    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    private CdiContainer cdiContainer;

    @Override
    protected void before() throws Throwable {
        cdiContainer = DeltaspikeBundle.bootCdiContainer();
    }

    @Override
    protected void after() {
        cdiContainer.shutdown();
    }
}
