package de.holisticon.dropwizard.deltaspike.example.jersey;

import static org.assertj.core.api.Assertions.*;

import de.holisticon.dropwizard.deltaspike.DeltaspikeBundle;
import de.holisticon.dropwizard.deltaspike.example.DummyResource;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Test;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.ws.rs.core.Application;

public class JerseyDeltaspikeSpike extends JerseyTest {

    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    private CdiContainer cdiContainer;

    @Override
    protected Application configure() {
        cdiContainer = DeltaspikeBundle.bootCdiContainer();
        assertThat(cdiContainer).isNotNull();

        return new ResourceConfig(DummyResource.class);
    }

    @Test
    public void test() {
        assertThat(target(DummyResource.ROOT_PATH).request().get(String.class)).startsWith("hello foo");
    }

    @After
    public void after() {
        cdiContainer.shutdown();
    }
}
