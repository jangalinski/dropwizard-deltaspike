package de.holisticon.dropwizard.deltaspike.jersey;

import de.holisticon.dropwizard.deltaspike.example.DummyResource;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.cdise.api.ContextControl;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.After;
import org.junit.Test;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.ws.rs.core.Application;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

public class JerseyDeltaspikeSpike extends JerseyTest {

    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    private CdiContainer cdiContainer;

    @Override
    protected Application configure() {
        cdiContainer = CdiContainerLoader.getCdiContainer();
        assertThat(cdiContainer).isNotNull();
        cdiContainer.boot();
        cdiContainer.getContextControl().startContext(RequestScoped.class);
        cdiContainer.getContextControl().startContext(SessionScoped.class);
        cdiContainer.getContextControl().startContext(ApplicationScoped.class);
        cdiContainer.getContextControl().startContext(ConversationScoped.class);
        return new ResourceConfig(DummyResource.class);
    }

    @Test
    public void test() {
        assertThat(target(DummyResource.ROOT_PATH).request().get(String.class)).isEqualTo("hello bar");
    }

    @After
    public void after() {
        cdiContainer.shutdown();
    }
}