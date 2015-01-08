package de.holisticon.dropwizard.deltaspike.example;

import com.codahale.metrics.health.HealthCheck;

public class DummyHealthCheck extends HealthCheck {

    @Override
    protected Result check() throws Exception {
        return Result.healthy("alles ok");
    }
}
