package de.holisticon.dropwizard.deltaspike.example;

import org.slf4j.Logger;

import javax.enterprise.event.Observes;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by jangalinski on 09.01.15.
 */
public class FooLogger {

    public static class Foo {
        public String hello() {return "================ i am in foo!";}
    }

    private final Logger logger = getLogger(this.getClass());

    public void logFoo(@Observes Foo foo) {
        logger.error(foo.hello());
    }
}
