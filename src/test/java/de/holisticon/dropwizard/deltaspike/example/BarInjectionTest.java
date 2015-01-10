package de.holisticon.dropwizard.deltaspike.example;

import static org.assertj.core.api.Assertions.*;

import de.holisticon.dropwizard.deltaspike.test.CdiContainerRule;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.junit.ClassRule;
import org.junit.Test;

import javax.inject.Inject;

public class BarInjectionTest {

    @ClassRule
    public static final CdiContainerRule CDI_CONTAINER_RULE = new CdiContainerRule();

    @Inject
    private DummyResource.Bar bar;

    @Test
    public void injects_bar_with_internal_foo() {
        BeanProvider.injectFields(this);

        assertThat(bar.get()).startsWith("foo");
    }
}
