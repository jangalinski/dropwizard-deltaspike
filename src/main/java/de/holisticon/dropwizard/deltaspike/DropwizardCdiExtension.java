package de.holisticon.dropwizard.deltaspike;

import org.slf4j.Logger;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import static org.slf4j.LoggerFactory.getLogger;

class DropwizardCdiExtension implements Extension {

    private final Logger logger = getLogger(this.getClass());

    void beforeBeanDiscovery(@Observes BeforeBeanDiscovery bbd) {
        logger.error("============================> beginning the scanning process");
    }


    <T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> pat) {
        logger.error("============================> scanning type: " + pat.getAnnotatedType().getJavaClass().getName());
    }

    void afterBeanDiscovery(@Observes AfterBeanDiscovery abd) {
        logger.error("============================> finished the scanning process");

    }
}
