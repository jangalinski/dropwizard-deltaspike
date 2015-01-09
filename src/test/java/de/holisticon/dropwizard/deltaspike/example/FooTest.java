package de.holisticon.dropwizard.deltaspike.example;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.needle4j.annotation.Mock;
import org.needle4j.annotation.ObjectUnderTest;
import org.needle4j.junit.NeedleBuilders;
import org.needle4j.junit.NeedleRule;
import org.needle4j.junit.NeedleRuleBuilder;

import javax.inject.Inject;

/**
 * Created by jangalinski on 09.01.15.
 */
public class FooTest {

    public static class Foo {
        @Inject
        private Bar bar;

        public String hello() { return bar.getText();}
    }

    public static  interface Bar {
        String getText();
    }

    @Rule
    public final NeedleRule needle = NeedleBuilders.needleMockitoRule().build();

    // needle erzeugt eine Instanz unserer CuT und kümmert sich um DI, dabei wird automatisch ein Mock von Bar erzeugt.
    @ObjectUnderTest
    private Foo foo;

    // Wir lassen uns die Instanz des Mocks injecten um sein Verhalten zu modifizieren
    @Inject
    private Bar bar;

    @Test
    public void foo_returns_text_from_bar() {
        when(bar.getText()).thenReturn("hello from bar");
        assertThat(foo.hello()).isEqualTo("hello from bar");
    }

}
