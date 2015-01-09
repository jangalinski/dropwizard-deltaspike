package de.holisticon.dropwizard.deltaspike.example;


import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.ClassRule;
import org.junit.Test;

import static io.dropwizard.testing.ResourceHelpers.resourceFilePath;
import static org.assertj.core.api.Assertions.assertThat;

public class DeltaspikeExampleApplicationTest {

    @ClassRule
    public static final DropwizardAppRule<DeltaspikeExampleApplication.Config> RULE =
            new DropwizardAppRule<DeltaspikeExampleApplication.Config>(DeltaspikeExampleApplication.class, resourceFilePath("example.yaml"));


    @Test
        public void _() {
	assertThat(true).isTrue();
        }
}
