package de.holisticon.dropwizard.deltaspike.example;

import com.google.common.base.Supplier;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

@Path(DummyResource.ROOT_PATH)
@Produces(MediaType.APPLICATION_JSON)
public class DummyResource {

    public static final String ROOT_PATH = "/dummy";

    public static class Bar implements Supplier<String> {

        @Inject
        @Named("foo")
        private String foo;

        @Override
        public String get() {
            return foo;
        }
    }

    @Inject
    private Bar bar;

    @GET
    public String helloWorld() {
        return "hello " + bar.get();
    }

}
